package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class AiGeneratedListing(
    val title: String,
    val hindiTitle: String,
    val category: String,
    val shortDescription: String,
    val englishDescription: String,
    val hindiDescription: String,
    val tags: String,
    val suggestedPrice: Double,
    val whatsappCaption: String
)

class GeminiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun generateListing(rawInput: String, district: String): AiGeneratedListing = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = """
                    You are an expert AI Assistant for Padharo, Rajasthan's Local Artisan & Small Business Marketplace.
                    A local seller/artisan from $district provided this description of their product in Hindi/Hinglish/English:
                    "$rawInput"
                    
                    Generate a high-converting, authentic, culturally respectful product listing.
                    Return ONLY valid JSON with keys:
                    - "title": (Concise English product title)
                    - "hindiTitle": (Hindi product title)
                    - "category": (One of: Handicrafts & Decor, Blue Pottery, Textiles & Bandhej, Terracotta & Clay, Wood Carvings, Food & Spices, Lac Bangles & Jewelry, Leather Crafts)
                    - "shortDescription": (1 snappy sentence in English)
                    - "englishDescription": (Rich 2-3 sentence description emphasizing Rajasthani craftsmanship, materials, and utility)
                    - "hindiDescription": (Natural Hindi description for local buyers)
                    - "tags": (Comma-separated search keywords)
                    - "suggestedPrice": (Numeric value based on input or fair craft price, e.g. 450)
                    - "whatsappCaption": (Ready-to-share WhatsApp broadcast message with emojis and greeting)
                    
                    Do NOT invent fake GI-tag certification or organic claims unless stated by the seller.
                """.trimIndent()

                val requestJson = JSONObject().apply {
                    val contentsArray = JSONArray().apply {
                        val contentObj = JSONObject().apply {
                            val partsArray = JSONArray().apply {
                                put(JSONObject().apply { put("text", prompt) })
                            }
                            put("parts", partsArray)
                        }
                        put(contentObj)
                    }
                    put("contents", contentsArray)

                    val generationConfig = JSONObject().apply {
                        put("responseMimeType", "application/json")
                        put("temperature", 0.4)
                    }
                    put("generationConfig", generationConfig)
                }

                // Using gemini-3.1-flash-lite-preview for ultra-fast seller responses
                val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.1-flash-lite-preview:generateContent?key=$apiKey"
                val requestBody = requestJson.toString().toRequestBody("application/json".toMediaType())
                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {
                    val rootJson = JSONObject(responseBody)
                    val candidates = rootJson.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val text = candidates.getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text")

                        val parsed = JSONObject(text.trim())
                        return@withContext AiGeneratedListing(
                            title = parsed.optString("title", rawInput),
                            hindiTitle = parsed.optString("hindiTitle", rawInput),
                            category = parsed.optString("category", "Handicrafts & Decor"),
                            shortDescription = parsed.optString("shortDescription", ""),
                            englishDescription = parsed.optString("englishDescription", rawInput),
                            hindiDescription = parsed.optString("hindiDescription", rawInput),
                            tags = parsed.optString("tags", "Rajasthan,Handmade,$district"),
                            suggestedPrice = parsed.optDouble("suggestedPrice", extractPrice(rawInput)),
                            whatsappCaption = parsed.optString("whatsappCaption", "नमस्ते! पधारो राजस्थान पर हमारा नया हस्तनिर्मित उत्पाद उपलब्ध है।")
                        )
                    }
                }
            } catch (e: Exception) {
                Log.w("GeminiService", "API call failed, fallback to local synthesizer: ${e.message}")
            }
        }

        // Local Smart Synthesizer fallback
        synthesizeLocally(rawInput, district)
    }

    private fun extractPrice(text: String): Double {
        val regex = Regex("""(?i)(?:rs\.?|inr|₹|price|रुपये|कीमत)\s*(\d+)""")
        val match = regex.find(text)
        if (match != null) {
            return match.groupValues[1].toDoubleOrNull() ?: 450.0
        }
        val numberRegex = Regex("""\b(\d{2,5})\b""")
        val numMatch = numberRegex.find(text)
        return numMatch?.groupValues?.get(1)?.toDoubleOrNull() ?: 450.0
    }

    private fun synthesizeLocally(input: String, district: String): AiGeneratedListing {
        val lower = input.lowercase()
        val detectedPrice = extractPrice(input)

        val (category, titleEn, titleHi, descEn, descHi) = when {
            lower.contains("bamboo") || lower.contains("बांस") || lower.contains("basket") || lower.contains("टोकरी") -> {
                Tuple5(
                    "Handicrafts & Decor",
                    "Authentic Handwoven Bamboo Craft from $district",
                    "$district की पारंपरिक हाथ से बुनी प्राकृतिक बांस की टोकरी",
                    "Handcrafted by local tribal artisans using sustainable green bamboo. Naturally cured with neem extracts for lasting strength, perfect for everyday utility and eco-conscious home decor.",
                    "स्थानीय जनजातीय कारीगरों द्वारा शुद्ध बांस से हस्तनिर्मित। टिकाऊ, पर्यावरण के अनुकूल और घरेलू उपयोग व सजावट के लिए आदर्श।",
                )
            }
            lower.contains("pottery") || lower.contains("पॉटरी") || lower.contains("vase") || lower.contains("फूलदान") -> {
                Tuple5(
                    "Blue Pottery",
                    "Traditional Glazed Craft Decorative Piece ($district)",
                    "$district की प्रामाणिक हस्तनिर्मित नक्काशीदार कलाकृति",
                    "Hand-shaped by master craftsmen featuring authentic mineral glazes and floral artwork. Baked in wood kilns to achieve royal luster.",
                    "कुशल कारीगरों द्वारा चाक पर तैयार और प्राकृतिक रंगों से चित्रित। राजस्थान की समृद्ध कला परंपरा का बेजोड़ नमूना।",
                )
            }
            lower.contains("chudi") || lower.contains("bangle") || lower.contains("चूड़ी") || lower.contains("लाख") -> {
                Tuple5(
                    "Lac Bangles & Jewelry",
                    "Traditional Royal Handcrafted Lac Bangles Set",
                    "पारंपरिक हस्तनिर्मित राजस्थानी लाख की चूड़ियां",
                    "Handmade from pure forest lac melted over charcoal and embellished with sparkling stones. Skin-friendly, hypoallergenic, and culturally auspicious.",
                    "प्राकृतिक लाख से पारंपरिक विधि द्वारा निर्मित और कुंदन नगों से सुसज्जित। शुभ अवसरों और दैनिक सौंदर्य के लिए उत्तम।",
                )
            }
            lower.contains("saree") || lower.contains("dupatta") || lower.contains("साड़ी") || lower.contains("दुपट्टा") || lower.contains("bandhej") || lower.contains("बंधेज") -> {
                Tuple5(
                    "Textiles & Bandhej",
                    "Handloom Traditional Tie-Dye Textile from $district",
                    "$district का पारंपरिक हथकरघा बंधेज परिधान",
                    "Woven with fine breathable fabric and dyed using age-old Rajasthani resist-dye methods. Celebrates vibrant desert colors and soft drape.",
                    "शुद्ध सूती ताने-बाने पर पारंपरिक बंधेज व लहरिया रंगाई से तैयार। आरामदायक, हवादार और राजस्थानी संस्कृति का प्रतीक।",
                )
            }
            lower.contains("wood") || lower.contains("लकड़ी") || lower.contains("jharokha") || lower.contains("झरोखा") -> {
                Tuple5(
                    "Wood Carvings",
                    "Solid Wood Hand-Carved Royal Heritage Piece",
                    "लकड़ी पर बारीक नक्काशीदार पारंपरिक शाही झरोखा",
                    "Chiseled by heritage woodworkers out of seasoned timber. Features palace-inspired arch geometry with a rich antique hand-wax polish.",
                    "मेहरानगढ़ और आमेर के महलों से प्रेरित लकड़ी की बारीक हस्तकला। आपके घर की दीवार को दे राजपुताना शान।",
                )
            }
            else -> {
                Tuple5(
                    "Handicrafts & Decor",
                    "Artisanal $district Handcrafted Specialty",
                    "$district का प्रामाणिक स्थानीय हस्तनिर्मित उत्पाद",
                    "Carefully crafted by independent local artisans in $district, Rajasthan. Directly supporting rural livelihood and cultural heritage.",
                    "$district के स्थानीय कारीगरों द्वारा पारंपरिक कला से बनाया गया। सीधे कारीगर से आपके घर तक।",
                )
            }
        }

        val tags = "Rajasthan,Handmade,$district,VocalForLocal,AuthenticCraft"
        val whatsapp = """
            🙏 खम्मा घणी! 
            पधारो राजस्थान (Padharo) पर हमारा नया उत्पाद देखें:
            ✨ $titleHi ($titleEn)
            📍 जिला: $district, राजस्थान
            💰 कीमत: ₹${detectedPrice.toInt()}
            📦 शुद्ध स्थानीय कारीगरों द्वारा निर्मित। सीधे ऑर्डर करें!
        """.trimIndent()

        return AiGeneratedListing(
            title = titleEn,
            hindiTitle = titleHi,
            category = category,
            shortDescription = "Authentic handcrafted local creation from $district.",
            englishDescription = descEn,
            hindiDescription = descHi,
            tags = tags,
            suggestedPrice = detectedPrice,
            whatsappCaption = whatsapp
        )
    }

    private data class Tuple5<A, B, C, D, E>(val a: A, val b: B, val c: C, val d: D, val e: E)
}

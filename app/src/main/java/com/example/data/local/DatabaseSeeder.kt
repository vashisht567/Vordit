package com.example.data.local

import com.example.data.local.entities.OrderEntity
import com.example.data.local.entities.OrderItemEntity
import com.example.data.local.entities.PlatformSettingsEntity
import com.example.data.local.entities.ProductEntity
import com.example.data.local.entities.ProductReviewEntity
import com.example.data.local.entities.SellerProfileEntity
import com.example.data.local.entities.UserEntity

object DatabaseSeeder {

    val defaultUsers = listOf(
        UserEntity(
            id = "usr_customer_1",
            name = "Sunita Sharma",
            email = "sunita.sharma@example.com",
            phone = "+91 98290 11223",
            role = "CUSTOMER",
            district = "Jaipur",
            profilePhotoUrl = ""
        ),
        UserEntity(
            id = "usr_seller_1",
            name = "Shanti Devi",
            email = "shanti.devi@padharo.in",
            phone = "+91 94140 55667",
            role = "SELLER",
            district = "Banswara",
            profilePhotoUrl = ""
        ),
        UserEntity(
            id = "usr_admin_1",
            name = "Mission Director Padharo",
            email = "admin@padharo.rajasthan.in",
            phone = "+91 98290 99887",
            role = "ADMIN",
            district = "Jaipur",
            profilePhotoUrl = ""
        )
    )

    val defaultSellers = listOf(
        SellerProfileEntity(
            id = "sel_1",
            userId = "usr_seller_1",
            businessName = "Banswara Tribal Bamboo SHG",
            artisanName = "Shanti Devi & Tribal Artisans",
            category = "Handicrafts & Decor",
            district = "Banswara",
            villageOrCity = "Kushalgarh Village",
            fullAddress = "Gram Panchayat Bhawan, Kushalgarh, Banswara, Rajasthan 327801",
            businessDescription = "A self-help group of 25 tribal women creating sustainable bamboo handicrafts, woven baskets, and home decor using locally harvested bamboo.",
            artisanStory = "Shanti Devi learned traditional bamboo weaving from her grandmother in the forested hills of Banswara. Through Padharo, her self-help group now ships directly to urban homes without city middlemen, keeping 100% of proceeds in their tribal village.",
            craftOrigin = "Ancestral Bhil tribal wicker & bamboo craft passed down through 4 generations along the Mahi river basin.",
            videoUrl = "https://padharo.rajasthan.gov.in/reels/banswara_bamboo_shg.mp4",
            videoTitle = "Splitting Wild Bamboo & Hand-Weaving Storage Baskets in Kushalgarh",
            videoDurationSeconds = 48,
            status = "APPROVED",
            isPro = true,
            rating = 4.9f,
            reviewCount = 28
        ),
        SellerProfileEntity(
            id = "sel_2",
            userId = "usr_seller_2",
            businessName = "Mohanlal Jaipur Blue Pottery Guild",
            artisanName = "Mohanlal Prajapat",
            category = "Blue Pottery",
            district = "Jaipur",
            villageOrCity = "Kotputli / Amber Road",
            fullAddress = "Shilpgram Lane 4, Near Amer Road, Jaipur, Rajasthan 302002",
            businessDescription = "3rd generation blue pottery master craftsman producing glazed ceramics, quartz-paste decorative plates, and vases with Persian turquoise motifs.",
            artisanStory = "Carrying forward the 150-year royal blue pottery tradition of Maharaja Sawai Ram Singh II with non-toxic lead-free natural glazes. Each piece is crafted from ground quartz stone without using clay.",
            craftOrigin = "150-year royal court quartz glazed blue pottery tradition tracing back to Maharaja Sawai Ram Singh II.",
            videoUrl = "https://padharo.rajasthan.gov.in/reels/jaipur_blue_pottery.mp4",
            videoTitle = "On the Potter's Wheel: Hand-painting Turquoise Persian Motifs",
            videoDurationSeconds = 55,
            status = "APPROVED",
            isPro = false,
            rating = 4.8f,
            reviewCount = 42
        ),
        SellerProfileEntity(
            id = "sel_3",
            userId = "usr_seller_3",
            businessName = "Desert Bloom Ajrakh Weavers",
            artisanName = "Fatema Khatun & Guild",
            category = "Textiles & Bandhej",
            district = "Barmer",
            villageOrCity = "Barmer Desert Craft Cluster",
            fullAddress = "Near Gandhi Nagar Craft Hub, Barmer, Rajasthan 344001",
            businessDescription = "Traditional double-sided resist-dye block printing on natural handloom cotton using pomegranate peel, madder, and organic indigo dyes.",
            artisanStory = "Every Ajrakh pattern carries geometric celestial astronomy motifs handed down over 400 years across Thar desert communities, washed meticulously in desert riverbeds.",
            craftOrigin = "Over 400-year-old Sindhi-Marwari double-sided natural resist block printing using river water and indigo roots.",
            videoUrl = "https://padharo.rajasthan.gov.in/reels/barmer_ajrakh_printing.mp4",
            videoTitle = "Master Carver Fatema Khatun Block-Printing 14-Stage Ajrakh Cloth",
            videoDurationSeconds = 62,
            status = "APPROVED",
            isPro = true,
            rating = 4.9f,
            reviewCount = 35
        ),
        SellerProfileEntity(
            id = "sel_4",
            userId = "usr_seller_4",
            businessName = "Pokhran Golden Terracotta Guild",
            artisanName = "Ramswaroop Kumhar",
            category = "Terracotta & Clay",
            district = "Jaisalmer",
            villageOrCity = "Pokhran Village",
            fullAddress = "Kumhar Vada, Pokhran, Jaisalmer, Rajasthan 345021",
            businessDescription = "Eco-friendly porous clay pots, desert cooling matkas, and earthen cookware fired using traditional desert kilns.",
            artisanStory = "The distinctive red clay of Pokhran has natural mineral properties that naturally cool and mineralize drinking water even in 48-degree summers.",
            craftOrigin = "Mineral-rich red desert terracotta pottery of Pokhran, naturally cooling water for desert travelers for centuries.",
            videoUrl = "https://padharo.rajasthan.gov.in/reels/pokhran_terracotta.mp4",
            videoTitle = "Throwing Red Clay Matkas on Foot-Driven Wheel in Pokhran",
            videoDurationSeconds = 39,
            status = "APPROVED",
            isPro = false,
            rating = 4.7f,
            reviewCount = 19
        ),
        SellerProfileEntity(
            id = "sel_5",
            userId = "usr_seller_5",
            businessName = "Marwar Heritage Wood Carvers",
            artisanName = "Govind Singh Rathore",
            category = "Wood Carvings",
            district = "Jodhpur",
            villageOrCity = "Mandore Crafts Enclave",
            fullAddress = "Plot 12, Mandore Industrial Craft Area, Jodhpur, Rajasthan 342007",
            businessDescription = "Hand-carved reclaimed Sheesham and Acacia wood jharokha mirrors, vintage chests, and royal palace architectural accents.",
            artisanStory = "Creating heirloom timber pieces inspired by Mehrangarh Fort's intricate filigree woodwork, seasoned naturally under the desert sun.",
            craftOrigin = "Jodhpur royal palace architectural wood carving tradition using sustainably salvaged Sheesham timber.",
            videoUrl = "https://padharo.rajasthan.gov.in/reels/jodhpur_wood_jharokha.mp4",
            videoTitle = "Chiseling Intricate Floral Jali on Sheesham Timber",
            videoDurationSeconds = 50,
            status = "APPROVED",
            isPro = false,
            rating = 4.9f,
            reviewCount = 22
        ),
        SellerProfileEntity(
            id = "sel_6",
            userId = "usr_seller_6",
            businessName = "Dungarpur Tribal Forest Honey & Amla SHG",
            artisanName = "Sunita Meena",
            category = "Food & Spices",
            district = "Dungarpur",
            villageOrCity = "Sagwara Tribal Zone",
            fullAddress = "Sagwara Block, Dungarpur, Rajasthan 314025",
            businessDescription = "Wild multi-flora forest honey, sun-dried desi amla candy, and organic cold-pressed mustard oil harvested by tribal women.",
            artisanStory = "Seeking marketplace approval to supply pure raw forest honey from the Vagad hills directly to healthy families across India.",
            craftOrigin = "Vagad indigenous wild hive extraction preserving forest bee populations without artificial heating or sugar syrup.",
            videoUrl = "",
            videoTitle = "",
            videoDurationSeconds = 0,
            status = "PENDING", // PENDING for Admin approval testing
            isPro = false,
            rating = 5.0f,
            reviewCount = 0
        )
    )

    val defaultProducts = listOf(
        ProductEntity(
            id = "prod_1",
            sellerId = "sel_1",
            name = "Banswara Tribal Handwoven Bamboo Fruit & Decor Basket",
            hindiName = "बांसवाड़ा जनजातीय हस्तनिर्मित बांस की टोकरी",
            category = "Handicrafts & Decor",
            description = "Handcrafted with natural green bamboo sourced from Vagad forests by women of Kushalgarh SHG. Treated naturally with neem oil for durability. Ideal for fresh fruits, breads, or bohemian room decor.",
            hindiDescription = "कुशलगढ़ महिला स्वयं सहायता समूह द्वारा वागड़ के बांस से प्राकृतिक रूप से बुनी गई टिकाऊ और सुंदर टोकरी। फलों और सजावट के लिए उत्तम।",
            shortDescription = "Eco-friendly, durable handwoven tribal bamboo basket.",
            price = 450.0,
            discountPercent = 10,
            stock = 18,
            sku = "BSW-BAM-01",
            weightGrams = 420,
            dimensions = "28x28x14 cm",
            imageUrl = "bamboo_basket",
            district = "Banswara",
            makerStory = "Woven by Shanti Devi and tribal artisan women in Banswara using ancestral interlacing techniques.",
            makerName = "Shanti Devi (Banswara Tribal SHG)",
            tags = "Bamboo,Tribal,Handmade,EcoFriendly,Banswara,Baskets",
            isApproved = true,
            isFeatured = true,
            rating = 4.9f,
            reviewCount = 14,
            isDemo = true
        ),
        ProductEntity(
            id = "prod_2",
            sellerId = "sel_2",
            name = "Jaipur Royal Blue Pottery Turquoise Floral Vase",
            hindiName = "जयपुर रॉयल ब्लू पॉटरी फिरोज़ा फ्लोरल फूलदान",
            category = "Blue Pottery",
            description = "Traditional Jaipur Blue Pottery crafted without clay using quartz powder, fuller's earth, and natural mineral pigments. Features cobalt and turquoise Persian botanical patterns fired at 800°C.",
            hindiDescription = "क्वार्ट्ज और प्राकृतिक खनिजों से बना प्रामाणिक जयपुर ब्लू पॉटरी फूलदान। कोबाल्ट और फिरोज़ी शाही फारसी नक्काशी।",
            shortDescription = "Authentic GI-heritage glazed ceramic craft from Jaipur.",
            price = 890.0,
            discountPercent = 15,
            stock = 12,
            sku = "JPR-BP-02",
            weightGrams = 850,
            dimensions = "15x15x25 cm",
            imageUrl = "blue_pottery_vase",
            district = "Jaipur",
            makerStory = "Shaped on the wheel and hand-painted freehand by Master Mohanlal Prajapat.",
            makerName = "Mohanlal Prajapat",
            tags = "BluePottery,Jaipur,Ceramic,HandPainted,Vase,HomeDecor",
            isApproved = true,
            isFeatured = true,
            rating = 4.8f,
            reviewCount = 21,
            isDemo = true
        ),
        ProductEntity(
            id = "prod_3",
            sellerId = "sel_3",
            name = "Barmer Indigo Ajrakh Hand-Block Printed Cotton Stole",
            hindiName = "बाड़मेर शुद्ध इंडिगो अजरख ब्लॉक प्रिंट सूती दुपट्टा",
            category = "Textiles & Bandhej",
            description = "Printed with hand-carved teakwood blocks using pure organic indigo and wild madder root. 100% fine mulmul breathable cotton, featuring intricate 16-stage resist dye craftsmanship.",
            hindiDescription = "प्राकृतिक नील और मंजीठ से हाथ के लकड़ी के ठप्पों द्वारा तैयार बाड़मेर का असली अजरख कॉटन दुपट्टा।",
            shortDescription = "Hand-blocked natural dye mulmul cotton dupatta.",
            price = 720.0,
            discountPercent = 8,
            stock = 25,
            sku = "BMR-AJK-03",
            weightGrams = 200,
            dimensions = "220x90 cm",
            imageUrl = "ajrakh_stole",
            district = "Barmer",
            makerStory = "Crafted by Fatema Khatun's artisan family over 14 days of desert sun curing and river washing.",
            makerName = "Fatema Khatun (Desert Bloom)",
            tags = "Ajrakh,HandBlock,Barmer,OrganicIndigo,CottonDupatta,Textiles",
            isApproved = true,
            isFeatured = true,
            rating = 4.9f,
            reviewCount = 18,
            isDemo = true
        ),
        ProductEntity(
            id = "prod_4",
            sellerId = "sel_2",
            name = "Handmade Rajasthani Lac Bangles Set (Kundan Studded)",
            hindiName = "हस्तनिर्मित राजस्थानी लाख की चूड़ियां (कुंदन जड़ी)",
            category = "Handicrafts & Decor",
            description = "Natural lac collected from Dhak trees, melted over charcoal embers, rolled by hand, and embellished with miniature kundan stones and brass sequins. Skin-safe and hypoallergenic.",
            hindiDescription = "चारकोल की आंच पर प्राकृतिक लाख से तैयार पारंपरिक कुंदन जड़ी राजस्थानी चूड़ियों का सेट।",
            shortDescription = "Set of 4 traditional artisanal royal lac bangles.",
            price = 420.0,
            discountPercent = 0,
            stock = 30,
            sku = "JPR-LAC-04",
            weightGrams = 180,
            dimensions = "Size 2.6 (Medium)",
            imageUrl = "lac_bangles",
            district = "Jaipur",
            makerStory = "Shaped by third-generation Manihar lac artisans in the historic walled city of Jaipur.",
            makerName = "Manihar Craft Cooperative",
            tags = "LacBangles,Jewelry,Jaipur,Kundan,Handmade,Traditional",
            isApproved = true,
            isFeatured = false,
            rating = 4.7f,
            reviewCount = 9,
            isDemo = true
        ),
        ProductEntity(
            id = "prod_5",
            sellerId = "sel_4",
            name = "Pokhran Red Terracotta Water Dispenser Pot with Clay Spigot",
            hindiName = "पोकरण लाल मिट्टी का मटका (प्राकृतिक ठंडा पानी)",
            category = "Terracotta & Clay",
            description = "Crafted with micro-porous desert clay from Pokhran that chills drinking water naturally through thermodynamic evaporation while balancing water pH with vital minerals.",
            hindiDescription = "पोकरण की बालू मिट्टी से चाक पर बना शुद्ध मटका, जो बिना फ्रिज के पानी को ठंडा और अमृत समान मीठा बनाता है।",
            shortDescription = "Traditional natural cooling red clay water pot.",
            price = 380.0,
            discountPercent = 5,
            stock = 15,
            sku = "JSL-PKH-05",
            weightGrams = 1400,
            dimensions = "24x24x32 cm (7 Liters)",
            imageUrl = "clay_pot",
            district = "Jaisalmer",
            makerStory = "Turned on a stone potter's wheel by Ramswaroop Kumhar using centuries-old desert firing techniques.",
            makerName = "Ramswaroop Kumhar",
            tags = "Terracotta,ClayMatka,Pokhran,Jaisalmer,EcoCooling,Pottery",
            isApproved = true,
            isFeatured = false,
            rating = 4.8f,
            reviewCount = 11,
            isDemo = true
        ),
        ProductEntity(
            id = "prod_6",
            sellerId = "sel_5",
            name = "Jodhpur Hand-Carved Sheesham Wood Jharokha Mirror",
            hindiName = "जोधपुर शीशम की लकड़ी का नक्काशीदार झरोखा",
            category = "Wood Carvings",
            description = "Carved out of solid seasoned Indian Rosewood (Sheesham) with vintage distressed brass hardware. Detailed floral arch work reminiscent of royal Mehrangarh palace balconies.",
            hindiDescription = "मेहरानगढ़ शैली में शीशम की मजबूत लकड़ी पर बारीक छेनी-हथौड़े से तराशा गया शाही झरोखा दर्पण।",
            shortDescription = "Heritage royal palace jharokha wooden mirror.",
            price = 1850.0,
            discountPercent = 12,
            stock = 8,
            sku = "JDH-JHK-06",
            weightGrams = 2200,
            dimensions = "38x52x6 cm",
            imageUrl = "wooden_jharokha",
            district = "Jodhpur",
            makerStory = "Hand-carved with chisels by master woodcarver Govind Singh Rathore in Jodhpur.",
            makerName = "Govind Singh Rathore",
            tags = "WoodCarving,Jharokha,Sheesham,Jodhpur,RoyalDecor,Mirror",
            isApproved = true,
            isFeatured = true,
            rating = 5.0f,
            reviewCount = 15,
            isDemo = true
        ),
        ProductEntity(
            id = "prod_7",
            sellerId = "sel_5",
            name = "Udaipur Traditional Pichwai Sacred Cow Painting on Tussar Silk",
            hindiName = "उदयपुर पिछवाई पेंटिंग - पवित्र कामधेनु गाय (सिल्क पर)",
            category = "Handicrafts & Decor",
            description = "Intricate hand-painted Pichwai portraying Shrinathji's blessed Kamadhenu cows grazing beside Kadamba trees and lotus ponds. Rendered with natural stone pigments and gold leaf powder.",
            hindiDescription = "उदयपुर चित्रकारों द्वारा तुषार सिल्क कपड़े पर प्राकृतिक पत्थरों के रंगों और सोने की स्याही से उकेरी गई पिछवाई।",
            shortDescription = "Original hand-painted Pichwai artwork on silk.",
            price = 1550.0,
            discountPercent = 10,
            stock = 7,
            sku = "UDP-PCH-07",
            weightGrams = 250,
            dimensions = "45x60 cm",
            imageUrl = "pichwai_painting",
            district = "Udaipur",
            makerStory = "Painted with squirrel-hair brushes by Nathdwara and Udaipur temple lineage artists.",
            makerName = "Udaipur Chitrashala",
            tags = "Pichwai,Painting,SilkArt,Udaipur,Shrinathji,SpiritualDecor",
            isApproved = true,
            isFeatured = true,
            rating = 4.9f,
            reviewCount = 8,
            isDemo = true
        ),
        ProductEntity(
            id = "prod_8",
            sellerId = "sel_3",
            name = "Kota Doria Handloom Tissue Zari Border Saree",
            hindiName = "कोटा डोरिया हथकरघा जरी बॉर्डर साड़ी",
            category = "Textiles & Bandhej",
            description = "Woven on traditional pit-looms in Kaithun cluster near Kota. Unique checkered 'khat' weave with pure cotton and silk warp, accented with pure golden zari work border.",
            hindiDescription = "कैथून के बुनकरों द्वारा हाथ के गड्ढा-लूम पर बुनी गई हल्की, हवादार और शाही कोटा डोरिया साड़ी।",
            shortDescription = "Lightweight handloom cotton-silk Kota Doria saree.",
            price = 2200.0,
            discountPercent = 18,
            stock = 14,
            sku = "KOT-DOR-08",
            weightGrams = 480,
            dimensions = "6.3 meters with blouse",
            imageUrl = "kota_saree",
            district = "Kota",
            makerStory = "Woven over 6 days by master weaver families in Kaithun village, Kota.",
            makerName = "Kaithun Handloom Weavers Guild",
            tags = "KotaDoria,HandloomSaree,Kota,Zari,CottonSilk,HeritageWear",
            isApproved = true,
            isFeatured = false,
            rating = 4.8f,
            reviewCount = 13,
            isDemo = true
        )
    )

    val defaultReviews = listOf(
        ProductReviewEntity(
            productId = "prod_1",
            userId = "usr_customer_1",
            userName = "Sunita Sharma",
            userDistrict = "Jaipur",
            rating = 5,
            reviewText = "Amazing quality bamboo work! Knowing this was handwoven by Shanti Devi in Banswara makes it so special. Packed with biodegradable paper.",
            isVerifiedPurchase = true
        ),
        ProductReviewEntity(
            productId = "prod_2",
            userId = "usr_customer_1",
            userName = "Vikram Rathore",
            userDistrict = "Jodhpur",
            rating = 5,
            reviewText = "Authentic Jaipur Blue Pottery. The turquoise color is vibrant and glossy. Arrived safely without any damage.",
            isVerifiedPurchase = true
        ),
        ProductReviewEntity(
            productId = "prod_6",
            userId = "usr_customer_1",
            userName = "Ananya Iyer",
            userDistrict = "Bengaluru",
            rating = 5,
            reviewText = "The Sheesham carving is breathtaking. Real Rajasthani craftsmanship, heavy wood and solid joinery.",
            isVerifiedPurchase = true
        )
    )

    val defaultOrders = listOf(
        OrderEntity(
            id = "ORD-RJ-2026-101",
            userId = "usr_customer_1",
            customerName = "Sunita Sharma",
            customerPhone = "+91 98290 11223",
            deliveryAddress = "Flat 402, Royal Palms, Vaishali Nagar, Jaipur",
            district = "Jaipur",
            subtotal = 450.0,
            deliveryFee = 0.0,
            totalAmount = 450.0,
            paymentMethod = "UPI (Google Pay)",
            paymentStatus = "PAID",
            orderStatus = "DELIVERED",
            trackingNumber = "IND-PADHARO-882319",
            notes = "Delivered to customer"
        )
    )

    val defaultOrderItems = listOf(
        OrderItemEntity(
            orderId = "ORD-RJ-2026-101",
            productId = "prod_1",
            sellerId = "sel_1",
            productName = "Banswara Tribal Handwoven Bamboo Fruit Basket",
            productImageUrl = "bamboo_basket",
            price = 450.0,
            quantity = 1
        )
    )

    suspend fun seedDatabase(database: AppDatabase) {
        val userDao = database.userDao()
        val sellerDao = database.sellerDao()
        val productDao = database.productDao()
        val reviewDao = database.reviewDao()
        val orderDao = database.orderDao()
        val settingsDao = database.settingsDao()

        // Seed users
        for (user in defaultUsers) {
            userDao.insertUser(user)
        }

        // Seed sellers
        for (seller in defaultSellers) {
            sellerDao.insertSeller(seller)
        }

        // Seed products
        for (product in defaultProducts) {
            productDao.insertProduct(product)
        }

        // Seed reviews
        for (review in defaultReviews) {
            reviewDao.insertReview(review)
        }

        // Seed initial order
        for (order in defaultOrders) {
            orderDao.insertOrder(order)
        }
        orderDao.insertOrderItems(defaultOrderItems)

        // Seed platform settings
        if (settingsDao.getSettingsDirect() == null) {
            settingsDao.insertSettings(PlatformSettingsEntity())
        }
    }
}

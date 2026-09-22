# Padharo: Rajasthan Rural Artisan Direct-to-Consumer Marketplace 🏰✨

**पधारो (Padharo)** is a modern Android application engineered with **Jetpack Compose**, **Kotlin Coroutines & Flow**, **Room Database**, and **Material Design 3**. It connects authentic rural craft artisans, tribal self-help groups (SHGs), and generational master craftspeople from Rajasthan directly with urban conscious buyers, bypassing middlemen markups and providing 100% fair-trade transparency.

---

## 🌟 Key Features

### 1. "Meet the Maker" & Ancestral Heritage System
- **Prominent Product Page Feature**: Every product prominently showcases the artisan's personal story, ancestral lineage, and workshop location.
- **Craft Origin & Lineage**: Detailed documentation of traditional heritage techniques (e.g. 4th-generation tribal bamboo wicker craft along Mahi river, 150-year royal court quartz blue pottery, 400-year double-sided Ajrakh natural resist printing).
- **Interactive Workshop Reel & Making Video**:
  - Embedded short video player with play/pause simulation, timecode scrub bar, and audio toggle.
  - Fullscreen immersive documentary mode with bilingual subtitles (Hindi & English).
  - 1-tap "Send Namaste & Artisan Appreciation 🙏" interaction.
- **Direct-to-Artisan Fair Trade Guarantee**: Transparency metrics showing 100% direct proceeds and 0 middlemen markups.

### 2. Seller Profile & Video Management (Seller Dashboard)
- **Approved Seller Story Studio**: Approved artisans can add/edit their personal story, business/SHG name, district, village, and craft origin.
- **Curated Workshop Reel Presets & Custom URL Support**:
  - One-tap selection of certified Rajasthani craft reels (Bamboo weaving, Blue Pottery wheel throwing, Ajrakh indigo printing, Pokhran clay matkas, Jodhpur wood carving).
  - Custom video URL and duration configuration.
- **Live Preview Mode**: Sellers can preview their "Meet the Maker" card in real-time before publishing updates.

### 3. Rajasthan District Exploration & Geographical GI Craft Hub
- District-wise catalog covering Banswara, Jaipur, Barmer, Jodhpur, Jaisalmer, Udaipur, Kota, Dungarpur, and more.
- District badges, craft categories, and GI-certified origin tags.

### 4. Bilingual Support & Direct Commerce
- **Full Hindi (हिंदी) & English (EN)** instant localization across the entire app.
- Role switching (Customer, Seller, Admin) for testing complete marketplace workflows.
- Cart, wishlist, order tracking, and seller order fulfillment pipeline (Packed -> Shipped).

---

## 🛠️ Tech Stack & Architecture

- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material Design 3 (M3)
- **Architecture**: MVVM (Model-View-ViewModel) + Repository Pattern + Clean Data Layer
- **Local Persistence**: [Room Database](https://developer.android.com/training/data-storage/room) with KSP (Kotlin Symbol Processing)
- **Asynchronous Operations**: Kotlin Coroutines + StateFlow / Flow
- **Navigation**: Jetpack Compose Navigation
- **Image & Media Loading**: Coil Compose
- **Design System**: Rajasthani Cultural Palette (Royal Saffron `#C95A0C`, Royal Indigo `#1B2A4A`, Oasis Green `#2D7D46`, Star Gold `#D4A31E`)

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug / Meerkat (or newer)
- JDK 17 or higher
- Android SDK 34+

### Clone & Build
```bash
git clone https://github.com/<your-username>/padharo-rajasthan.git
cd padharo-rajasthan

# Build debug APK
gradle assembleDebug

# Run unit tests
gradle :app:testDebugUnitTest
```

---

## 📜 License
Developed for rural craft empowerment and ethical direct-to-consumer e-commerce.

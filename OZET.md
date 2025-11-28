# SCBPano Android TV Uygulaması - Hızlı Başlangıç

## 📋 Proje Yapısı

```
AndroidTVApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/scbpano/tv/
│   │   │   └── MainActivity.java    # Ana aktivite (WebView)
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   └── activity_main.xml
│   │   │   ├── values/
│   │   │   │   ├── strings.xml
│   │   │   │   └── colors.xml
│   │   │   └── mipmap-hdpi/
│   │   │       └── ic_launcher.xml
│   │   └── AndroidManifest.xml
│   ├── build.gradle                 # Uygulama bağımlılıkları
│   └── proguard-rules.pro
├── build.gradle                     # Proje seviyesi build dosyası
├── settings.gradle
├── gradle.properties
├── README.md                        # Genel bilgiler
├── KURULUM.md                      # Detaylı kurulum talimatları
└── OZET.md                         # Bu dosya
```

## 🚀 Hızlı Başlangıç (3 Adım)

### 1. Android Studio'yu Hazırlayın
- Android Studio'yu indirip kurun
- SDK Manager'dan Android SDK Platform 34'ü kurun

### 2. Projeyi Açın
```bash
# Android Studio'da:
File → Open → AndroidTVApp klasörünü seçin
```

### 3. APK Oluşturun
```bash
# Android Studio'da:
Build → Build Bundle(s) / APK(s) → Build APK(s)
```

APK şu konumda oluşur:
```
app/build/outputs/apk/release/app-release.apk
```

## ⚙️ Yapılandırma

### URL Değiştirme
`app/src/main/java/com/scbpano/tv/MainActivity.java` dosyasında:
```java
private static final String WEB_URL = "https://samsunkml.com";
```
Bu satırı düzenleyerek farklı bir URL kullanabilirsiniz.

### Uygulama Adı Değiştirme
`app/src/main/res/values/strings.xml` dosyasında:
```xml
<string name="app_name">SCBPano</string>
```

### Versiyon Güncelleme
`app/build.gradle` dosyasında:
```gradle
versionCode 1
versionName "1.0.0"
```

## 📱 Android TV'ye Yükleme

### En Kolay Yöntem: USB
1. APK'yı USB belleğe kopyalayın
2. Android TV'ye takın
3. Ayarlar → Güvenlik → Bilinmeyen kaynaklar → AÇ
4. APK'yı bulup yükleyin

## ✨ Özellikler

- ✅ Tam ekran mod (Immersive mode)
- ✅ Android TV Leanback desteği
- ✅ Otomatik yatay ekran yönlendirmesi
- ✅ İnternet bağlantısı kontrolü
- ✅ Geri tuşu ile sayfa geri gitme
- ✅ Çift geri tuşu ile uygulamadan çıkma
- ✅ Ekranı açık tutma
- ✅ Video otomatik oynatma desteği

## 🔧 Teknik Detaylar

- **Minimum SDK:** 21 (Android 5.0 Lollipop)
- **Target SDK:** 34 (Android 14)
- **Orientasyon:** Landscape (Yatay)
- **WebView:** JavaScript, DOM Storage, Cache aktif
- **İnternet:** HTTPS ve HTTP desteği

## 📚 Daha Fazla Bilgi

- Detaylı kurulum için: `KURULUM.md`
- Genel bilgiler için: `README.md`

## ⚠️ Önemli Notlar

1. **İnternet Bağlantısı:** Uygulama çalışmak için internet bağlantısı gerektirir
2. **URL:** Uygulamanın açacağı URL doğru yapılandırılmalı
3. **Bilinmeyen Kaynaklar:** Android TV'de APK yüklemek için bu ayar açık olmalı
4. **Güncelleme:** Her yeni APK'da `versionCode` artırılmalı

## 🐛 Sorun Giderme

**APK yüklenmiyor?**
→ Bilinmeyen kaynaklar ayarını kontrol edin

**Web sayfası açılmıyor?**
→ İnternet bağlantısını ve URL'yi kontrol edin

**Tam ekran çalışmıyor?**
→ Uygulamayı kapatıp tekrar açın

Daha fazla sorun giderme bilgisi için `KURULUM.md` dosyasına bakın.


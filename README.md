# SCBPano Android TV Uygulaması

Bu Android TV uygulaması, SCBPano web sayfasını tam ekran olarak Android TV'de göstermek için oluşturulmuştur.

## 🚀 Hızlı Başlangıç

### GitHub Actions ile Otomatik APK Oluşturma (Önerilen)

**Android Studio kurmadan** GitHub Actions ile otomatik APK oluşturun:

1. **Projeyi GitHub'a yükleyin** (`GITHUB_SETUP.md` dosyasına bakın)
2. **Actions sekmesine gidin** ve `Build Android APK` workflow'unu çalıştırın
3. **APK'yı indirin** - Artifacts bölümünden `app-release.apk` dosyasını alın

Detaylı bilgi: `CLOUD_BUILD.md`

### Yerel Build (Android Studio)

Android Studio ile yerel olarak APK oluşturmak için `KURULUM.md` dosyasına bakın.

## 📋 Gereksinimler

- **GitHub Actions için:** Sadece GitHub hesabı (ücretsiz)
- **Yerel build için:** Android Studio (Arctic Fox veya daha yeni)

## ⚙️ Yapılandırma

### URL Değiştirme

`app/src/main/java/com/scbpano/tv/MainActivity.java` dosyasında:
```java
private static final String WEB_URL = "https://samsunkml.com";
```

### Versiyon Güncelleme

`app/build.gradle` dosyasında:
```gradle
versionCode 2  // Her yeni APK için artırın
versionName "1.0.1"
```

## 📱 Android TV'ye Yükleme

1. APK'yı USB belleğe kopyalayın
2. Android TV'ye takın
3. Ayarlar → Güvenlik → Bilinmeyen kaynaklar → AÇ
4. APK'yı bulup yükleyin

Detaylı talimatlar: `KURULUM.md`

## ✨ Özellikler

- ✅ Tam ekran modu (Immersive mode)
- ✅ Android TV Leanback desteği
- ✅ Otomatik yatay ekran yönlendirmesi
- ✅ İnternet bağlantısı kontrolü
- ✅ Geri tuşu ile sayfa geri gitme
- ✅ Çift geri tuşu ile uygulamadan çıkma
- ✅ Ekranı açık tutma
- ✅ Video otomatik oynatma desteği

## 📚 Dokümantasyon

- **`CLOUD_BUILD.md`** - GitHub Actions ile otomatik build
- **`GITHUB_SETUP.md`** - GitHub repository kurulumu
- **`KURULUM.md`** - Detaylı kurulum ve yükleme talimatları
- **`OZET.md`** - Hızlı başlangıç kılavuzu

## 🔧 Teknik Detaylar

- **Minimum SDK:** 21 (Android 5.0 Lollipop)
- **Target SDK:** 34 (Android 14)
- **Orientasyon:** Landscape (Yatay)
- **WebView:** JavaScript, DOM Storage, Cache aktif
- **İnternet:** HTTPS ve HTTP desteği

## 🐛 Sorun Giderme

**APK yüklenmiyor?**
→ Bilinmeyen kaynaklar ayarını kontrol edin

**Web sayfası açılmıyor?**
→ İnternet bağlantısını ve URL'yi kontrol edin

**Tam ekran çalışmıyor?**
→ Uygulamayı kapatıp tekrar açın

**GitHub Actions build hatası?**
→ `GITHUB_SETUP.md` dosyasındaki sorun giderme bölümüne bakın

## 📝 Lisans

Bu proje SCBPano için özel olarak geliştirilmiştir.

## 📞 Destek

Sorunlar için GitHub Issues kullanabilir veya geliştirici ile iletişime geçebilirsiniz.

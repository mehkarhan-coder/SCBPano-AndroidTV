# Android TV APK Kurulum Talimatları

## Gereksinimler

### 1. Android Studio Kurulumu
- Android Studio'yu [buradan](https://developer.android.com/studio) indirin ve kurun
- Minimum Android Studio Arctic Fox (2020.3.1) veya daha yeni sürüm

### 2. Android SDK Kurulumu
Android Studio açıldıktan sonra:
1. `Tools` → `SDK Manager` seçin
2. Aşağıdaki paketleri kurun:
   - Android SDK Platform 34
   - Android SDK Build-Tools
   - Android TV SDK

## Projeyi Açma

1. Android Studio'yu açın
2. `File` → `Open` seçin
3. `AndroidTVApp` klasörünü seçin
4. Gradle sync işleminin tamamlanmasını bekleyin (birkaç dakika sürebilir)

## URL Yapılandırması

Uygulamanın açacağı URL'yi değiştirmek için:
1. `app/src/main/java/com/scbpano/tv/MainActivity.java` dosyasını açın
2. 29. satırdaki `WEB_URL` değişkenini düzenleyin:
   ```java
   private static final String WEB_URL = "https://samsunkml.com";
   ```
3. Değişiklikleri kaydedin

## APK Oluşturma (Build)

### Release APK Oluşturma

1. Android Studio'da `Build` menüsünden `Build Bundle(s) / APK(s)` → `Build APK(s)` seçin
2. Build işlemi tamamlandığında, ekranda bir bildirim görünecek
3. "locate" linkine tıklayın veya şu konuma gidin:
   ```
   app/build/outputs/apk/release/app-release.apk
   ```

### Signed APK Oluşturma (Play Store için)

1. `Build` → `Generate Signed Bundle / APK` seçin
2. `APK` seçeneğini seçin ve `Next` tıklayın
3. Keystore dosyası seçin veya yeni bir tane oluşturun
4. İmzalama bilgilerini girin
5. `release` build variant'ını seçin ve `Finish` tıklayın

## GitHub Actions ile Otomatik APK Oluşturma (Bulut)

GitHub Actions kullanarak Android Studio'ya gerek kalmadan otomatik APK oluşturabilirsiniz:

1. Projeyi GitHub repository'sine yükleyin
2. Repository'nin `Actions` sekmesine gidin
3. `Build Android APK` workflow'unu seçin ve `Run workflow` butonuna tıklayın
4. APK oluşturulduktan sonra `Artifacts` bölümünden indirin

Detaylı bilgi için `CLOUD_BUILD.md` dosyasına bakın.

## Android TV'ye Yükleme

### Yöntem 1: USB ile Yükleme

1. APK dosyasını USB belleğe kopyalayın
2. USB belleği Android TV'ye takın
3. Android TV'de `Ayarlar` → `Güvenlik ve kısıtlamalar` → `Bilinmeyen kaynaklar` seçeneğini açın
4. USB bellekteki APK dosyasını bulun ve yükleyin

### Yöntem 2: ADB ile Yükleme (Geliştirici Modu)

1. Android TV'de `Ayarlar` → `Cihaz Tercihleri` → `Hakkında` bölümüne gidin
2. `Build` numarasına 7 kez basarak geliştirici modunu aktifleştirin
3. `Ayarlar` → `Cihaz Tercihleri` → `Geliştirici seçenekleri` → `USB hata ayıklama` seçeneğini açın
4. Bilgisayarınızda ADB kurulu olduğundan emin olun
5. Terminal/Command Prompt'ta şu komutu çalıştırın:
   ```bash
   adb connect [TV_IP_ADRESI]
   adb install app-release.apk
   ```

### Yöntem 3: Ağ Üzerinden Yükleme

1. APK'yı bir web sunucusuna yükleyin veya ağ paylaşımına koyun
2. Android TV'de bir tarayıcı açın ve APK'nın URL'sine gidin
3. APK'yı indirin ve yükleyin

## Test Etme

1. Android TV'de uygulamayı başlatın
2. Web sayfasının tam ekran yüklendiğini kontrol edin
3. Navigasyon tuşlarının çalıştığını test edin
4. İnternet bağlantısının olup olmadığını kontrol edin

## Sorun Giderme

### APK Yüklenmiyor
- Android TV'nin "Bilinmeyen kaynaklar" ayarının açık olduğundan emin olun
- APK'nın Android TV ile uyumlu olduğundan emin olun (minimum SDK 21)

### Web Sayfası Yüklenmiyor
- İnternet bağlantısını kontrol edin
- URL'nin doğru olduğundan emin olun (`MainActivity.java` dosyasında)
- Android TV'nin DNS ayarlarını kontrol edin

### Tam Ekran Çalışmıyor
- Android TV'nin sistem ayarlarından "Tam ekran uygulamalar" iznini kontrol edin
- Uygulamayı kapatıp tekrar açmayı deneyin

## Özellikler

✅ Tam ekran modu (Immersive mode)
✅ Android TV Leanback launcher desteği
✅ Yatay (landscape) ekran yönlendirmesi
✅ İnternet bağlantısı kontrolü
✅ Otomatik hata yönetimi
✅ Geri tuşu ile gezinme
✅ Çift geri tuşu ile çıkış

## Güncelleme

Uygulamayı güncellemek için:
1. Yeni APK'yı aynı şekilde oluşturun
2. Versiyon kodunu (`versionCode`) artırın (`app/build.gradle` dosyasında)
3. APK'yı Android TV'ye yükleyin (eski sürümün üzerine)

## Notlar

- Uygulama, web sayfasını tam ekran modda gösterir
- Kullanıcı etkileşimi için gerekli tüm JavaScript özellikleri etkindir
- Video ve medya içeriği otomatik olarak oynatılabilir
- Uygulama, Android TV'nin tüm navigasyon tuşlarını destekler


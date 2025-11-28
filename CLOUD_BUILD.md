# GitHub Actions ile Otomatik APK Oluşturma

Bu kılavuz, GitHub Actions kullanarak Android Studio Cloud üzerinden otomatik APK oluşturma işlemini açıklar.

## 🚀 Hızlı Başlangıç

### 1. GitHub Repository Oluşturma

1. GitHub'da yeni bir repository oluşturun
2. Projenizi repository'ye yükleyin:
   ```bash
   cd AndroidTVApp
   git init
   git add .
   git commit -m "Initial commit: Android TV App"
   git branch -M main
   git remote add origin https://github.com/KULLANICI_ADI/REPO_ADI.git
   git push -u origin main
   ```

### 2. GitHub Actions Workflow

Proje zaten `.github/workflows/build-apk.yml` dosyası ile yapılandırılmıştır. Bu dosya:
- ✅ Kod push edildiğinde otomatik APK oluşturur
- ✅ Pull Request'lerde APK oluşturur
- ✅ Manuel tetikleme (workflow_dispatch) desteği

### 3. Otomatik APK Oluşturma

#### Yöntem 1: Otomatik (Push ile)
```bash
git add .
git commit -m "Update app"
git push
```
Push işleminden sonra GitHub Actions otomatik olarak APK oluşturur.

#### Yöntem 2: Manuel Tetikleme
1. GitHub repository'nizde `Actions` sekmesine gidin
2. Sol menüden `Build Android APK` workflow'unu seçin
3. Sağ üstteki `Run workflow` butonuna tıklayın
4. Branch seçin ve `Run workflow` butonuna tıklayın

### 4. APK'yı İndirme

APK oluşturulduktan sonra:

1. GitHub repository'nizde `Actions` sekmesine gidin
2. En üstteki (son) workflow run'unu seçin
3. `Artifacts` bölümünde `app-release-apk` linkine tıklayın
4. `app-release.apk` dosyasını indirin

**Not:** Artifact'ler 30 gün boyunca saklanır.

## 📋 Workflow Yapılandırması

Workflow dosyası şu özelliklere sahiptir:

### Tetikleyiciler
- **Push:** `main` veya `master` branch'ine push
- **Pull Request:** `main` veya `master` branch'ine PR
- **Manuel:** GitHub Actions arayüzünden manuel tetikleme

### Adımlar
1. ✅ Kod checkout
2. ✅ JDK 17 kurulumu
3. ✅ Android SDK kurulumu
4. ✅ Gradle permissions
5. ✅ APK build (release)
6. ✅ Artifact upload
7. ✅ (Opsiyonel) Release'e upload (tag push'larında)

## 🔧 Özelleştirme

### URL Değiştirme

APK'nın açacağı URL'yi değiştirmek için:

1. `app/src/main/java/com/scbpano/tv/MainActivity.java` dosyasını düzenleyin
2. `WEB_URL` değişkenini güncelleyin
3. Değişiklikleri commit edip push edin:
   ```bash
   git add .
   git commit -m "Update web URL"
   git push
   ```

### Build Ayarları

`app/build.gradle` dosyasında versiyon bilgilerini güncelleyin:

```gradle
defaultConfig {
    versionCode 2  // Her yeni APK için artırın
    versionName "1.0.1"
}
```

### Signed APK (İmzalı APK)

Play Store'a yüklemek için imzalı APK gerekir:

1. **Keystore Oluşturma:**
   ```bash
   keytool -genkey -v -keystore scbpano-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias scbpano
   ```

2. **Keystore'u GitHub Secrets'e Ekleyin:**
   - Repository → Settings → Secrets and variables → Actions
   - `KEYSTORE_FILE`: Base64 encoded keystore dosyası
   - `KEYSTORE_PASSWORD`: Keystore şifresi
   - `KEY_ALIAS`: Alias adı (örn: scbpano)
   - `KEY_PASSWORD`: Key şifresi

3. **Workflow Dosyasını Güncelleyin:**
   `.github/workflows/build-apk.yml` dosyasına keystore işlemlerini ekleyin.

## 📱 Release Oluşturma

Tag push'larında otomatik olarak Release oluşturulur:

```bash
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

Release oluşturulduktan sonra:
1. Repository → Releases
2. Yeni release'i göreceksiniz
3. APK dosyası release'e otomatik eklenecek

## 🔍 Sorun Giderme

### Build Hatası
- **Sorun:** Gradle sync hatası
- **Çözüm:** Workflow dosyasında JDK versiyonunu kontrol edin (17 kullanılıyor)

### APK Bulunamıyor
- **Sorun:** Artifact'te APK görünmüyor
- **Çözüm:** Workflow loglarını kontrol edin, build adımının başarılı olduğundan emin olun

### Android SDK Hatası
- **Sorun:** Android SDK kurulum hatası
- **Çözüm:** Workflow dosyasında `android-actions/setup-android@v2` action'ının güncel olduğundan emin olun

### İzin Hatası
- **Sorun:** Gradlew çalıştırılamıyor
- **Çözüm:** Workflow dosyasında `chmod +x gradlew` komutunun çalıştığından emin olun

## 📊 Build Durumu

Workflow çalışırken:
1. Repository ana sayfasında build durumu badge'i görünebilir
2. `Actions` sekmesinde tüm build geçmişi görünür
3. Her build için loglar ve artifact'ler mevcuttur

## 💡 İpuçları

1. **Hızlı Build:** Sadece değişiklik yaptığınızda push edin
2. **Test:** APK'yı indirip Android TV'de test edin
3. **Versiyonlama:** Her yeni APK'da versionCode'u artırın
4. **GitHub Actions Minutes:** Ücretsiz plan 2000 dakika/ay verir (yeterlidir)

## 🔐 Güvenlik

- Keystore dosyalarını **asla** repository'ye commit etmeyin
- Şifreleri GitHub Secrets'te saklayın
- `.gitignore` dosyasında keystore dosyalarını ignore edin

## 📚 Ek Kaynaklar

- [GitHub Actions Dokümantasyonu](https://docs.github.com/en/actions)
- [Android Build Dokümantasyonu](https://developer.android.com/studio/build)
- [Gradle Dokümantasyonu](https://docs.gradle.org)


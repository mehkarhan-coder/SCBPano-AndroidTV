# GitHub Repository Kurulum Kılavuzu

GitHub Actions ile otomatik APK oluşturmak için projenizi GitHub'a yüklemeniz gerekiyor.

## 📦 Senaryo 1: AndroidTVApp Klasörünü Doğrudan Repository Olarak Yükleme (ÖNERİLEN)

Bu yöntem, `AndroidTVApp` klasörünün içeriğini doğrudan GitHub repository root'una yükler.

### Adımlar:

1. **Yeni GitHub Repository Oluşturun:**
   - GitHub'da yeni bir repository oluşturun (örn: `SCBPano-AndroidTV`)

2. **AndroidTVApp Klasörüne Gidin:**
   ```bash
   cd AndroidTVApp
   ```

3. **Git Repository'yi Başlatın:**
   ```bash
   git init
   git add .
   git commit -m "Initial commit: Android TV App for SCBPano"
   ```

4. **GitHub Repository'sine Bağlayın:**
   ```bash
   git branch -M main
   git remote add origin https://github.com/KULLANICI_ADI/REPO_ADI.git
   git push -u origin main
   ```

**Bu yöntemde:** Workflow dosyası doğrudan çalışacaktır, path düzenlemesi gerekmez.

---

## 📦 Senaryo 2: Tüm Projeyi (SCBPano) Repository Olarak Yükleme

Eğer tüm SCBPano projesini (AndroidTVApp dahil) tek bir repository'de tutmak istiyorsanız:

### Adımlar:

1. **Ana Proje Klasörüne Gidin:**
   ```bash
   cd D:\Projeler\SCBPano_rar\SCBPano
   ```

2. **Git Repository'yi Başlatın:**
   ```bash
   git init
   git add .
   git commit -m "Initial commit: SCBPano Project with Android TV App"
   ```

3. **GitHub Repository'sine Bağlayın:**
   ```bash
   git branch -M main
   git remote add origin https://github.com/KULLANICI_ADI/REPO_ADI.git
   git push -u origin main
   ```

4. **Workflow Dosyasını Güncelleyin:**
   Bu durumda workflow dosyasını şu şekilde güncellemeniz gerekir:
   - `.github/workflows/build-apk.yml` dosyasını proje root'una taşıyın
   - Workflow dosyasındaki path'leri `AndroidTVApp/` ile başlatın

---

## ✅ Kontrol Listesi

- [ ] GitHub repository oluşturuldu
- [ ] Proje GitHub'a push edildi
- [ ] `.github/workflows/build-apk.yml` dosyası mevcut
- [ ] `gradlew` dosyası executable (chmod +x) olarak işaretlendi
- [ ] Repository → Settings → Actions → Workflow permissions → "Read and write permissions" seçili

## 🚀 İlk APK Oluşturma

1. **Manuel Tetikleme:**
   - GitHub repository'nizde `Actions` sekmesine gidin
   - Sol menüden `Build Android APK` workflow'unu seçin
   - `Run workflow` butonuna tıklayın
   - Branch seçin ve `Run workflow` butonuna tıklayın

2. **Otomatik Tetikleme:**
   - Herhangi bir değişiklik yapın ve push edin:
   ```bash
   git add .
   git commit -m "Trigger build"
   git push
   ```

3. **APK'yı İndirin:**
   - `Actions` sekmesinde workflow run'unu seçin
   - `Artifacts` bölümünden `app-release-apk` linkine tıklayın
   - `app-release.apk` dosyasını indirin

## 📝 Önemli Notlar

1. **Gradle Wrapper:** `gradlew` dosyası executable olmalı. GitHub Actions bunu otomatik yapacaktır.

2. **Secrets:** Eğer signed APK oluşturmak istiyorsanız, keystore bilgilerini GitHub Secrets'e ekleyin.

3. **Gitignore:** `.gitignore` dosyasında şunların ignore edildiğinden emin olun:
   - `*.iml`
   - `.gradle/`
   - `build/`
   - `local.properties`

4. **Disk Alanı:** GitHub Actions ücretsiz plan 2000 dakika/ay verir (yeterlidir).

## 🔍 Sorun Giderme

### "gradlew: command not found"
- `gradlew` dosyasının repository'ye eklendiğinden emin olun
- `git add gradlew` komutu ile ekleyin

### "Working directory not found"
- Workflow dosyasındaki path'leri kontrol edin
- Senaryo 1 kullanıyorsanız path'ler doğru olmalı
- Senaryo 2 kullanıyorsanız path'lere `AndroidTVApp/` ekleyin

### "Permission denied"
- `gradlew` dosyasının executable olduğundan emin olun
- Workflow dosyasında `chmod +x gradlew` komutu çalışıyor olmalı

## 📚 Sonraki Adımlar

APK başarıyla oluşturulduktan sonra:
1. APK'yı Android TV'ye yükleyin (KURULUM.md'ye bakın)
2. Test edin
3. Gerekirse URL'yi güncelleyin (`MainActivity.java`)
4. Yeni versiyon için `versionCode` artırın


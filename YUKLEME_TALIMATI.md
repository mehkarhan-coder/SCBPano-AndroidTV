# GitHub Repository'ye Yükleme Talimatları

Repository: `https://github.com/mehkarhan-coder/SCBPano-AndroidTV.git`

## 🚀 Adım Adım Yükleme

### 1. Terminal/Command Prompt Açın

Windows'ta:
- PowerShell veya Command Prompt açın
- AndroidTVApp klasörüne gidin

### 2. Git Kurulumu Kontrolü

```bash
git --version
```

Git yüklü değilse: https://git-scm.com/download/win

### 3. Git Repository'yi Başlatın

```bash
cd D:\Projeler\SCBPano_rar\SCBPano\AndroidTVApp
git init
```

### 4. Dosyaları Ekleyin

```bash
git add .
```

### 5. İlk Commit Oluşturun

```bash
git commit -m "Initial commit: SCBPano Android TV App"
```

**Not:** İlk commit için Git kullanıcı bilgileri gerekebilir:
```bash
git config --global user.name "Kullanıcı Adınız"
git config --global user.email "email@example.com"
```

### 6. Repository'ye Bağlayın

```bash
git branch -M main
git remote add origin https://github.com/mehkarhan-coder/SCBPano-AndroidTV.git
```

### 7. GitHub'a Yükleyin

```bash
git push -u origin main
```

**Not:** GitHub kimlik doğrulama gerekebilir:
- Personal Access Token (PAT) kullanın
- Veya GitHub Desktop uygulamasını kullanın

---

## 🔐 GitHub Kimlik Doğrulama

### Yöntem 1: Personal Access Token (PAT)

1. GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. "Generate new token (classic)" tıklayın
3. İzinleri seçin: `repo` (tüm repository işlemleri)
4. Token'ı kopyalayın
5. Push yaparken şifre yerine bu token'ı kullanın

### Yöntem 2: GitHub Desktop

1. GitHub Desktop uygulamasını indirin: https://desktop.github.com/
2. GitHub hesabınızla giriş yapın
3. "Add" → "Add Existing Repository" seçin
4. AndroidTVApp klasörünü seçin
5. "Publish repository" butonuna tıklayın

---

## ✅ Kontrol

Yükleme tamamlandıktan sonra:

1. https://github.com/mehkarhan-coder/SCBPano-AndroidTV adresine gidin
2. Dosyaların yüklendiğini kontrol edin
3. `.github/workflows/build-apk.yml` dosyasının mevcut olduğundan emin olun

---

## 🎯 İlk APK Oluşturma

1. **GitHub Actions ile:**
   - Repository sayfasında "Actions" sekmesine gidin
   - "Build Android APK" workflow'unu seçin
   - "Run workflow" → "Run workflow" butonuna tıklayın
   - Build tamamlandıktan sonra "Artifacts" bölümünden APK'yı indirin

2. **Otomatik (Her Push'ta):**
   ```bash
   git add .
   git commit -m "Update"
   git push
   ```
   Push sonrası otomatik olarak APK oluşturulur.

---

## 📝 Sonraki Adımlar

- ✅ Kod değişikliklerinde `git push` yapın
- ✅ Actions sekmesinden APK'yı indirin
- ✅ Android TV'ye yükleyin (`KURULUM.md` dosyasına bakın)

---

## 🐛 Sorun Giderme

### "fatal: remote origin already exists"
```bash
git remote remove origin
git remote add origin https://github.com/mehkarhan-coder/SCBPano-AndroidTV.git
```

### "Permission denied"
- Personal Access Token kullanın
- Veya SSH key kullanın

### "refusing to merge unrelated histories"
```bash
git pull origin main --allow-unrelated-histories
```

### "Authentication failed"
- GitHub hesabınızda 2FA aktifse Personal Access Token kullanın
- Token'ı şifre yerine kullanın


# ⚠️ GitHub Push İşlemi - Kimlik Doğrulama Gerekli

Repository'ye bağlantı yapıldı ancak push işlemi için GitHub kimlik doğrulama gerekiyor.

## 🔐 Çözüm: Personal Access Token (PAT) Kullanın

### Adım 1: Personal Access Token Oluşturun

1. **GitHub'a giriş yapın:** https://github.com/login
2. **Settings'e gidin:** Sağ üst köşede profil resminize tıklayın → Settings
3. **Developer settings:** Sol menüden en altta "Developer settings"
4. **Personal access tokens:** "Personal access tokens" → "Tokens (classic)"
5. **Yeni token oluştur:**
   - "Generate new token (classic)" butonuna tıklayın
   - Token için bir isim verin (örn: "SCBPano-AndroidTV")
   - Süre seçin (90 gün veya ihtiyacınıza göre)
   - **İzinleri seçin:** `repo` (tüm repository işlemleri) ✅
   - "Generate token" butonuna tıklayın
6. **Token'ı kopyalayın:** ⚠️ Bu token'ı bir daha göremeyeceksiniz, kaydedin!

### Adım 2: Token ile Push Yapın

PowerShell/Command Prompt'ta şu komutları çalıştırın:

```bash
cd D:\Projeler\SCBPano_rar\SCBPano\AndroidTVApp
git push -u origin main
```

**İstendiğinde:**
- Username: `mehkarhan-coder`
- Password: Oluşturduğunuz **Personal Access Token**'ı yapıştırın (şifre değil!)

---

## 🔄 Alternatif: Credential Manager ile Kaydetme

### Windows Credential Manager:

1. **Token ile bir kez push yaptıktan sonra:**
   ```bash
   git push -u origin main
   ```

2. **Windows Credential Manager'da kaydedin:**
   - Windows tuşu + R → `control /name Microsoft.CredentialManager`
   - "Windows Credentials" sekmesine gidin
   - `git:https://github.com` için kayıt görmelisiniz
   - Düzenleyip token'ı güncelleyin

### Git Credential Helper:

```bash
git config --global credential.helper manager-core
```

---

## ✅ Push İşlemi Başarılı Olduktan Sonra

1. **Repository'yi kontrol edin:**
   https://github.com/mehkarhan-coder/SCBPano-AndroidTV

2. **İlk APK'yı oluşturun:**
   - Repository sayfasında "Actions" sekmesine gidin
   - "Build Android APK" workflow'unu seçin
   - "Run workflow" → "Run workflow" butonuna tıklayın
   - Build tamamlandıktan sonra (5-10 dakika) "Artifacts" bölümünden APK'yı indirin

---

## 🐛 Sorun Giderme

### "remote: Permission denied"
- Personal Access Token kullandığınızdan emin olun
- Token'da `repo` izninin olduğunu kontrol edin

### "fatal: unable to access"
- İnternet bağlantınızı kontrol edin
- GitHub'ın erişilebilir olduğunu kontrol edin

### "Authentication failed"
- Token'ın süresinin dolmadığından emin olun
- Yeni bir token oluşturup tekrar deneyin

---

## 📝 Notlar

- Personal Access Token, GitHub şifrenizin yerine kullanılır
- Token'ı güvenli bir yerde saklayın
- Token'ı paylaşmayın veya commit etmeyin
- Token süresi dolduğunda yeni bir token oluşturmanız gerekir


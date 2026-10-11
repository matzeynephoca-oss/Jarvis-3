# Jarvis2 — Android başlangıç projesi

Bu proje Android tablet için temel bir Türkçe sesli komut uygulamasıdır. Şu an hazır bir APK değil, kaynak koddur; APK GitHub Actions ile derlenebilir.

## Mevcut özellikler
- Mikrofon düğmesiyle Türkçe sesli komut tanıma ve Türkçe sesli yanıt
- Saat ve pil yüzdesini söyleme
- WhatsApp, YouTube, Google ve Roblox uygulamasını/siteyi açma
- Google'da arama ve hava durumu araması
- Android bildirim erişimi açıldıktan sonra kaydedilen son WhatsApp bildirimini seslendirme

## APK oluşturma (tablet veya bilgisayar üzerinden)
1. GitHub hesabına giriş yap ve yeni bir **public veya private repository** oluştur; adı `jarvis2` olabilir.
2. Bu ZIP dosyasını aç. İçindeki dosya ve klasörleri (özellikle `.github` klasörünü de) depoya yükle.
3. GitHub deposunda **Actions** sekmesine gir.
4. **Build Jarvis2 APK** iş akışını seçip **Run workflow** düğmesine bas. İlk yüklemede otomatik de başlayabilir.
5. İşlem yeşil tik ile bittiğinde çalıştırmanın altındaki **Artifacts** bölümünden `Jarvis2-debug-apk` dosyasını indir.
6. ZIP'i aç; içindeki `app-debug.apk` dosyasını tablete gönderip aç. Android isterse yalnızca bu dosya için uygulama yükleme izni ver.

## İlk açılış
- Mikrofon iznini ver.
- WhatsApp bildirimlerini okutmak istiyorsan uygulamadaki bildirim erişimi düğmesine basıp Jarvis2'ye izin ver.
- Sesli komut için mikrofon düğmesine dokun. Örnek: “saat kaç”, “şarjım kaç”, “YouTube aç”, “WhatsApp aç”, “ara uzay haberleri”, “hava durumu”.

## Önemli sınırlar
- Bu sürüm genel amaçlı bir yapay zekâ sohbet motoru içermez; bilinmeyen soruları cevaplayamaz. Gerçek AI için güvenli bir sunucu/API entegrasyonu eklenmelidir; gizli API anahtarını APK içine koyma.
- WhatsApp'ın özel sohbet veritabanına doğrudan erişmez. Yalnızca Android'in izin verilen bildirimlerini okuyabilir; sessize alınmış veya bildirim göstermeyen mesajları okuyamayabilir.
- Sürekli arka planda “her zaman dinleme” bu sürümde yoktur. Android mikrofon ve arka plan erişimini sınırlar.
- Uygulama tüm dosyalara, hesaplara veya diğer uygulamaların özel verilerine erişemez; Android izinleri ve uygulama güvenlik sınırları geçerlidir.
- Derleme başarılı olsa bile APK gerçek tablette test edilmelidir.

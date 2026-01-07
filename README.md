# 📚 SmartLibraryPlus - ORM Tabanlı Kütüphane Yönetim Sistemi

Bu proje, **Nesneye Yönelik Programlama (OOP)** ve **ORM (Object Relational Mapping)** prensiplerine uygun olarak geliştirilmiş; **Java, Hibernate ve SQLite** teknolojilerini kullanan bir kütüphane otomasyon sistemidir.

---

## 👤 Öğrenci Bilgileri

* **Ad Soyad:** Ozan Baran Karakurt
* **Öğrenci Numarası:** 20230108035
* **Bölüm:** Bilgisayar Programcılığı
* **Ders:** NESNEYE DAYALI PROGRAMLAMA-II
* **Teslim Tarihi:** 07.01.2026

---

## 🎯 Projenin Amacı ve Senaryo

Bir üniversitenin kütüphane sistemini modernize etmek amacıyla geliştirilen bu projede, **JDBC kodları yerine tamamen Hibernate ORM** yapısı kullanılmıştır.

Projenin temel hedefleri:
* İlişkisel veritabanı yönetimini (RDBMS) nesne yönelimli olarak gerçekleştirmek.
* **DAO (Data Access Object)** tasarım desenini uygulamak.
* **Entity-Relationship** yapılarını (`@ManyToOne`, `@OneToMany`, `@OneToOne`) doğru kurgulamak.
* **CRUD** (Ekleme, Okuma, Güncelleme, Silme) operasyonlarını yönetmek.

---

## 🛠️ Kullanılan Teknolojiler ve Araçlar

* **Dil:** Java 17 (JDK 17)
* **ORM Framework:** Hibernate 6.4.0
* **Veritabanı:** SQLite (Gömülü Veritabanı)
* **Build Tool:** Maven
* **IDE:** IntelliJ IDEA
* **Log Yönetimi:** SLF4J (Konsol kirliliğini önlemek için yapılandırıldı)

---

## 📂 Proje Yapısı (Katmanlı Mimari)

Proje, sürdürülebilirlik ve temiz kod prensipleri gereği katmanlı mimariye (Layered Architecture) uygun tasarlanmıştır:

```text
SmartLibraryPlus/
├── src/main/java/
│   ├── entity/     # Veritabanı tablolarına karşılık gelen sınıflar (Book, Student, Loan)
│   ├── dao/        # Veritabanı erişim ve işlem sınıfları (BookDao, StudentDao...)
│   ├── util/       # Hibernate SessionFactory yardımcı sınıfı
│   └── app/        # Main sınıfı ve Konsol Menü arayüzü
├── src/main/resources/
│   ├── hibernate.cfg.xml   # Veritabanı ve Hibernate ayarları
│   └── simplelogger.properties # Log temizleme ayarları

└── pom.xml         # Maven bağımlılık yönetimi

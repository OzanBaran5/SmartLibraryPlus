package app;

import dao.BookDao;
import dao.LoanDao;
import dao.StudentDao;
import entity.Book;
import entity.Loan;
import entity.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "error");
        System.setProperty("org.slf4j.simpleLogger.log.org.hibernate", "error");
    }
    static Scanner scanner = new Scanner(System.in);
    static BookDao bookDao = new BookDao();
    static StudentDao studentDao = new StudentDao();
    static LoanDao loanDao = new LoanDao();

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n--- SmartLibrary2 Menü ---");
            System.out.println("1 - Kitap Ekle");
            System.out.println("2 - Kitapları Listele");
            System.out.println("3 - Öğrenci Ekle");
            System.out.println("4 - Öğrencileri Listele");
            System.out.println("5 - Kitap Ödünç Ver");
            System.out.println("6 - Ödünç Listesini Görüntüle");
            System.out.println("7 - Kitap Geri Teslim Al");
            System.out.println("0 - Çıkış");
            System.out.print("Seçiminiz: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Lütfen sayı giriniz!");
                continue;
            }

            switch (choice) {
                case 1: addBook(); break;
                case 2: listBooks(); break;
                case 3: addStudent(); break;
                case 4: listStudents(); break;
                case 5: borrowBook(); break;
                case 6: listLoans(); break;
                case 7: returnBook(); break;
                case 0:
                    System.out.println("Çıkış yapılıyor...");
                    System.exit(0);
                default:
                    System.out.println("Geçersiz seçim!");
            }
        }
    }

    private static void addBook() {
        System.out.print("Kitap Başlığı: ");
        String title = scanner.nextLine();
        System.out.print("Yazar: ");
        String author = scanner.nextLine();
        System.out.print("Yıl: ");
        int year = Integer.parseInt(scanner.nextLine());

        Book book = new Book(title, author, year);
        bookDao.save(book);
        System.out.println("✅ Kitap başarıyla eklendi.");
    }

    private static void listBooks() {
        List<Book> books = bookDao.getAll();
        if (books.isEmpty()) {
            System.out.println("Listelenecek kitap yok.");
        } else {
            System.out.println("\n--- KİTAP LİSTESİ ---");
            for (Book b : books) {
                System.out.println(b);
            }
        }
    }

    private static void addStudent() {
        System.out.print("Öğrenci Adı: ");
        String name = scanner.nextLine();
        System.out.print("Bölüm: ");
        String dept = scanner.nextLine();

        Student student = new Student(name, dept);
        studentDao.save(student);
        System.out.println("✅ Öğrenci başarıyla eklendi.");
    }

    private static void listStudents() {
        List<Student> students = studentDao.getAll();
        if (students.isEmpty()) {
            System.out.println("Kayıtlı öğrenci yok.");
        } else {
            System.out.println("\n--- ÖĞRENCİ LİSTESİ ---");
            for (Student s : students) {
                System.out.println(s);
            }
        }
    }

    private static void borrowBook() {
        System.out.print("Öğrenci ID: ");
        Long studentId;
        try {
            studentId = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Hatalı giriş! Sayı girmelisiniz.");
            return;
        }

        System.out.print("Kitap ID: ");
        Long bookId;
        try {
            bookId = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Hatalı giriş! Sayı girmelisiniz.");
            return;
        }

        Student student = studentDao.getById(studentId);
        Book book = bookDao.getById(bookId);

        if (student == null) {
            System.out.println("❌ Öğrenci bulunamadı.");
            return;
        }
        if (book == null) {
            System.out.println("❌ Kitap bulunamadı.");
            return;
        }


        if (book.getStatus() == Book.Status.BORROWED) {
            System.out.println("❌ Bu kitap şu an başkasında (Status: BORROWED)!");
            return;
        }

        try {
            Loan loan = new Loan(student, book, LocalDate.now());
            loanDao.save(loan);

            book.setStatus(Book.Status.BORROWED);
            bookDao.update(book);
            System.out.println("✅ Kitap öğrenciye ödünç verildi.");

        } catch (Exception e) {

            System.out.println("⚠️ İŞLEM BAŞARISIZ: OneToOne Kuralı İhlali!");
            System.out.println("Bu kitap daha önce bir kez ödünç verilmiş.");
            System.out.println("Ödevdeki 'Loan-Book OneToOne' kuralı gereği, bir kitap veritabanı ömrü boyunca sadece 1 kez ödünç tablosuna girebilir.");
        }
    }

    private static void listLoans() {
        List<Loan> loans = loanDao.getAll();
        if (loans.isEmpty()) {
            System.out.println("Hiç ödünç kaydı yok.");
        } else {
            System.out.println("\n--- ÖDÜNÇ GEÇMİŞİ ---");
            for (Loan l : loans) {
                System.out.println(l);
            }
        }
    }

    private static void returnBook() {
        System.out.print("İade edilecek Kitap ID: ");
        Long bookId = Long.parseLong(scanner.nextLine());

        Loan activeLoan = loanDao.getActiveLoanByBookId(bookId);

        if (activeLoan != null) {

            activeLoan.setReturnDate(LocalDate.now());
            loanDao.update(activeLoan);
            Book book = activeLoan.getBook();
            book.setStatus(Book.Status.AVAILABLE);
            bookDao.update(book);

            System.out.println("✅ Kitap iade alındı ve listeye eklendi.");
        } else {
            System.out.println("❌ Bu kitaba ait aktif bir ödünç kaydı bulunamadı.");
        }
    }
}
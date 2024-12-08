package pk1;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class BorrowCardList implements iprogram {
    private List<BorrowCard> borrowCards = new ArrayList<>();
    private static final String FILE_PATH = "test.txt";

    // Thêm thẻ mượn vào danh sách
    public void them() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Ten Nguoi Muon: ");
            String userName = scanner.nextLine();

            int bookCount = -1;
            while (bookCount < 0) {
                try {
                    System.out.print("So Luong Sach Muốn Muon: ");
                    bookCount = Integer.parseInt(scanner.nextLine());
                    if (bookCount < 0) {
                        System.out.println("Số lượng sách phải là số dương. Vui lòng nhập lại!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Dữ liệu không hợp lệ. Vui lòng nhập một số!");
                }
            }

            List<String> bookTitles = new ArrayList<>();
            for (int i = 1; i <= bookCount; i++) {
                System.out.print("Ten Sach Thu " + i + ": ");
                bookTitles.add(scanner.nextLine());
            }

            System.out.print("Nhap ID: ");
            String cardId = scanner.nextLine();

            Date borrowDate = null, returnDate = null;
            while (borrowDate == null) {
                try {
                    System.out.print("Ngay Muon (dd-MM-yyyy): ");
                    borrowDate = new SimpleDateFormat("dd-MM-yyyy").parse(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println("Ngày không hợp lệ. Vui lòng nhập theo định dạng dd-MM-yyyy!");
                }
            }
            while (returnDate == null) {
                try {
                    System.out.print("Ngay Tra (dd-MM-yyyy): ");
                    returnDate = new SimpleDateFormat("dd-MM-yyyy").parse(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println("Ngày không hợp lệ. Vui lòng nhập theo định dạng dd-MM-yyyy!");
                }
            }

            BorrowCard card = new BorrowCard(userName, bookTitles, cardId, borrowDate, returnDate);
            borrowCards.add(card);
            saveToFile();
            System.out.println("Them The Thanh Cong !");
            
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm thẻ mượn: " + e.getMessage());

        }
        
    }

    // Cập nhật thẻ mượn theo ID
    @Override
    public void sua() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Nhap ID the: ");
            String cardId = scanner.nextLine();

            BorrowCard existingCard = null;
            for (BorrowCard card : borrowCards) {
                if (card.getCardId().equals(cardId)) {
                    existingCard = card;
                    break;
                }
            }

            if (existingCard == null) {
                System.out.println("Khong tim thay ID the !");
                return;
            }

            System.out.println("Nhập thông tin mới:");

            System.out.print("Ten nguoi muon: ");
            String userName = scanner.nextLine();

            int bookCount = -1;
            while (bookCount < 0) {
                try {
                    System.out.print("So luong sach muon: ");
                    bookCount = Integer.parseInt(scanner.nextLine());
                    if (bookCount < 0) {
                        System.out.println("Số lượng sách phải là số dương. Vui lòng nhập lại!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Dữ liệu không hợp lệ. Vui lòng nhập một số!");
                }
            }

            List<String> bookTitles = new ArrayList<>();
            for (int i = 1; i <= bookCount; i++) {
                System.out.print("Tên sách thứ " + i + ": ");
                bookTitles.add(scanner.nextLine());
            }

            Date borrowDate = null, returnDate = null;
            while (borrowDate == null) {
                try {
                    System.out.print("Ngay muon (dd-MM-yyyy): ");
                    borrowDate = new SimpleDateFormat("dd-MM-yyyy").parse(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println("Ngày không hợp lệ. Vui lòng nhập theo định dạng dd-MM-yyyy!");
                }
            }
            while (returnDate == null) {
                try {
                    System.out.print("Ngay tra (dd-MM-yyyy): ");
                    returnDate = new SimpleDateFormat("dd-MM-yyyy").parse(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println("Ngày không hợp lệ. Vui lòng nhập theo định dạng dd-MM-yyyy!");
                }
            }

            existingCard.setUserName(userName);
            existingCard.setBookTitles(bookTitles);
            existingCard.setBorrowDate(borrowDate);
            existingCard.setReturnDate(returnDate);

            saveToFile();
            System.out.println("Cap nhat thanh cong!");
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật thẻ mượn: " + e.getMessage());
        }
    }

    // Xóa thẻ mượn theo ID
    @Override
    public void xoa() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Nhap ID the can xoa: ");
            String cardId = scanner.nextLine();
            if (borrowCards.removeIf(card -> card.getCardId().equals(cardId))) {
                saveToFile();
                System.out.println("Xoa the thanh cong !");
            } else {
                System.out.println("Khong tim thay ID the !");
            }
        }
    }
    // Hiển thị tất cả thẻ mượn
    @Override
    public void indanhsach() {
        if (borrowCards.isEmpty()) {
            System.out.println("Khong tim thay the nao.");
        } else {
            for (BorrowCard card : borrowCards) {
                System.out.println(card);
            }
        }
    }
    // Tìm kiếm thẻ mượn theo ID
    @Override
    public void tim() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhap ID can tim: ");
        String cardId = scanner.nextLine();
        for (BorrowCard card : borrowCards) {
            if (card.getCardId().equals(cardId)) {
                System.out.println(card);
                return;
            }
        }
        System.out.println("Khong tim thay ID.");
    }
    // Tải danh sách thẻ mượn từ file
    private void loadFromFile() {
        borrowCards.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 5) {
                    String userName = parts[0].split(":")[1];
                    List<String> bookTitles = Arrays.asList(parts[1].split(":")[1].split(";"));
                    String cardId = parts[2].split(":")[1];
                    Date borrowDate = new SimpleDateFormat("dd-MM-yyyy").parse(parts[3].split("=")[1]);
                    Date returnDate = new SimpleDateFormat("dd-MM-yyyy").parse(parts[4].split("=")[1].replace('}', ' ').trim());
                    borrowCards.add(new BorrowCard(userName, bookTitles, cardId, borrowDate, returnDate));
                }
            }
        } catch (IOException | java.text.ParseException e) {
            System.out.println("Loi load file: " + e.getMessage());
        }
    }

    // Lưu danh sách thẻ mượn vào file
    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (BorrowCard card : borrowCards) {
                writer.write(card.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi lưu file: " + e.getMessage());
        }
    }

    // Hiển thị menu quản lý
    public void menu1() {
        Scanner scanner = new Scanner(System.in);
        loadFromFile();
        while (true) {
            System.out.println("\nQuan Ly Mượn/Trả Tài Lieu");
            System.out.println("1. Them The Moi");
            System.out.println("2. Cap Nhat The");
            System.out.println("3. Xoa The");
            System.out.println("4. Hien Thi Danh Sach Cac The");
            System.out.println("5. Tim The");
            System.out.println("6. Thoat");
            System.out.print("Nhap lua chon: ");
    
            // Sửa lỗi xử lý đầu vào
            int choice = scanner.nextInt();
            scanner.nextLine(); // Xử lý ký tự xuống dòng còn sót lại
    
            switch (choice) {
                case 1 -> them();
                case 2 -> sua();
                case 3 -> xoa();
                case 4 -> indanhsach();
                case 5 -> tim();
                case 6 -> {
                    System.out.println("Thoát chương trình.");
                    return;
                }
                default -> System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        }
    }
    
}

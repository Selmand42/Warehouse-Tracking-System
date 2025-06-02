# Warehouse Tracking System

A comprehensive **warehouse management system** built in **Java** with **MySQL database** and **Swing GUI interface**. This application provides complete inventory management, user authentication, product tracking, and warehouse operations for efficient supply chain management.

## 📦 About the System

The Warehouse Tracking System is a desktop application designed to streamline warehouse operations and inventory management. It provides a user-friendly interface for managing products, warehouses, users, and tracking product movements across different storage locations.

### Key Capabilities

- 🏭 **Multi-Warehouse Management**: Handle multiple warehouse locations
- 📊 **Inventory Tracking**: Real-time product quantity and location monitoring
- 👥 **User Management**: Role-based access control and user administration
- 🔄 **Product Transfers**: Track product movements between warehouses
- 📋 **Reporting**: Generate inventory reports and transaction logs
- 🔐 **Secure Authentication**: User login and session management
- 💾 **Database Integration**: Persistent data storage with MySQL

## 🛠️ Technology Stack

### Core Technologies
- **Language**: Java (100%)
- **GUI Framework**: Java Swing
- **Database**: MySQL
- **Architecture**: MVC (Model-View-Controller) Pattern
- **Data Access**: DAO (Data Access Object) Pattern

### Key Components
- **Frontend**: Swing-based desktop GUI
- **Backend**: Java business logic layer
- **Database Layer**: MySQL with JDBC connectivity
- **Logging**: Custom logging system with LogHelper

## 🏗️ Project Architecture

```
warehouse-tracking-system/
├── Model Classes
│   ├── Kullanici.java          # User entity model
│   ├── Urun.java               # Product entity model
│   └── Depo.java               # Warehouse entity model
├── DAO Classes
│   ├── KullaniciDAO.java       # User data access operations
│   ├── UrunDAO.java            # Product data access operations
│   └── DepoDAO.java            # Warehouse data access operations
├── View Classes
│   ├── GirisEkrani.java        # Login screen interface
│   ├── KullaniciYonetimi.java  # User management interface
│   ├── UrunYonetimi.java       # Product management interface
│   └── UrunTransferi.java      # Product transfer interface
├── Table Models
│   ├── KullaniciTableModel.java # User table data model
│   └── UrunTableModel.java     # Product table data model
├── Utilities
│   ├── DatabaseConnection.java # Database connectivity handler
│   └── LogHelper.java          # Logging utility
└── Documentation
    └── uml.jpg                 # System UML diagram
```

## 🚀 Getting Started

### Prerequisites

- **Java Development Kit (JDK)** 8 or higher
- **MySQL Server** 5.7 or higher
- **MySQL JDBC Driver** (mysql-connector-java)
- **IDE** (IntelliJ IDEA, Eclipse, or NetBeans recommended)

### Installation Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Selmand42/Warehouse-Tracking-System.git
   cd Warehouse-Tracking-System
   ```

2. **Set Up MySQL Database**
   ```sql
   -- Create database
   CREATE DATABASE warehouse_tracking;
   USE warehouse_tracking;

   -- Create tables (see Database Schema section)
   ```

3. **Configure Database Connection**
   ```java
   // Update DatabaseConnection.java with your MySQL credentials
   private static final String URL = "jdbc:mysql://localhost:3306/warehouse_tracking";
   private static final String USERNAME = "your_username";
   private static final String PASSWORD = "your_password";
   ```

4. **Add MySQL JDBC Driver**
   - Download MySQL Connector/J from [MySQL official site](https://dev.mysql.com/downloads/connector/j/)
   - Add the JAR file to your project classpath

5. **Compile and Run**
   ```bash
   # Compile all Java files
   javac -cp ".:mysql-connector-java-8.0.x.jar" *.java

   # Run the application
   java -cp ".:mysql-connector-java-8.0.x.jar" GirisEkrani
   ```

## 🗄️ Database Schema

### Users Table (kullanicilar)
```sql
CREATE TABLE kullanicilar (
    id INT PRIMARY KEY AUTO_INCREMENT,
    kullanici_adi VARCHAR(50) UNIQUE NOT NULL,
    sifre VARCHAR(255) NOT NULL,
    ad VARCHAR(50) NOT NULL,
    soyad VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    rol ENUM('admin', 'operator', 'viewer') DEFAULT 'operator',
    olusturma_tarihi TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Products Table (urunler)
```sql
CREATE TABLE urunler (
    id INT PRIMARY KEY AUTO_INCREMENT,
    urun_kodu VARCHAR(50) UNIQUE NOT NULL,
    urun_adi VARCHAR(100) NOT NULL,
    kategori VARCHAR(50),
    birim VARCHAR(20) DEFAULT 'adet',
    minimum_stok INT DEFAULT 0,
    aciklama TEXT,
    olusturma_tarihi TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Warehouses Table (depolar)
```sql
CREATE TABLE depolar (
    id INT PRIMARY KEY AUTO_INCREMENT,
    depo_kodu VARCHAR(20) UNIQUE NOT NULL,
    depo_adi VARCHAR(100) NOT NULL,
    adres TEXT,
    kapasite INT,
    sorumlu_kisi VARCHAR(100),
    aktif BOOLEAN DEFAULT TRUE,
    olusturma_tarihi TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Inventory Table (stok)
```sql
CREATE TABLE stok (
    id INT PRIMARY KEY AUTO_INCREMENT,
    urun_id INT,
    depo_id INT,
    miktar INT NOT NULL DEFAULT 0,
    guncellenme_tarihi TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (urun_id) REFERENCES urunler(id),
    FOREIGN KEY (depo_id) REFERENCES depolar(id),
    UNIQUE KEY unique_urun_depo (urun_id, depo_id)
);
```

### Transfer Log Table (transfer_log)
```sql
CREATE TABLE transfer_log (
    id INT PRIMARY KEY AUTO_INCREMENT,
    urun_id INT,
    kaynak_depo_id INT,
    hedef_depo_id INT,
    miktar INT NOT NULL,
    kullanici_id INT,
    transfer_tarihi TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    aciklama TEXT,
    FOREIGN KEY (urun_id) REFERENCES urunler(id),
    FOREIGN KEY (kaynak_depo_id) REFERENCES depolar(id),
    FOREIGN KEY (hedef_depo_id) REFERENCES depolar(id),
    FOREIGN KEY (kullanici_id) REFERENCES kullanicilar(id)
);
```

## 🎯 Core Features

### 1. User Management (Kullanıcı Yönetimi)
- **User Authentication**: Secure login system
- **Role-Based Access**: Admin, operator, and viewer roles
- **User CRUD Operations**: Create, read, update, delete users
- **Profile Management**: User profile editing and password changes

### 2. Product Management (Ürün Yönetimi)
- **Product Catalog**: Comprehensive product database
- **Category Management**: Organize products by categories
- **Stock Levels**: Real-time inventory tracking
- **Product Search**: Find products by code, name, or category
- **Minimum Stock Alerts**: Low inventory notifications

### 3. Warehouse Management (Depo Yönetimi)
- **Multiple Warehouses**: Support for multiple storage locations
- **Warehouse Profiles**: Location details and capacity management
- **Inventory by Location**: Track stock levels per warehouse
- **Warehouse Reports**: Detailed inventory reports per location

### 4. Product Transfer (Ürün Transferi)
- **Inter-Warehouse Transfers**: Move products between warehouses
- **Transfer History**: Complete audit trail of all movements
- **Batch Transfers**: Transfer multiple products at once
- **Transfer Validation**: Ensure sufficient stock before transfers

### 5. Reporting and Analytics
- **Inventory Reports**: Current stock levels and valuations
- **Transfer Reports**: Product movement history
- **User Activity Logs**: Track user actions and changes
- **Low Stock Alerts**: Automated notifications for minimum stock levels

## 🖥️ User Interface

### Login Screen (Giriş Ekranı)
- Secure user authentication
- Session management
- Remember login option
- Error handling for invalid credentials

### Main Dashboard
- Overview of warehouse status
- Quick access to all modules
- Real-time inventory summary
- Recent activities log

### Product Management Interface
- Product listing with search and filter
- Add/Edit product forms
- Stock level indicators
- Bulk operations support

### Transfer Management
- Transfer wizard interface
- Real-time stock validation
- Transfer history tracking
- Confirmation dialogs

## 🔧 Configuration

### Database Configuration
```java
// DatabaseConnection.java
public class DatabaseConnection {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/warehouse_tracking";
    private static final String USERNAME = "your_db_username";
    private static final String PASSWORD = "your_db_password";
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
}
```

### Logging Configuration
The system uses a custom logging mechanism through `LogHelper.java` for tracking:
- User login/logout events
- Product transfers
- Inventory changes
- System errors and exceptions

## 🧪 Testing

### Unit Testing
```bash
# Test database connectivity
java DatabaseConnection

# Test user authentication
java KullaniciDAO

# Test product operations
java UrunDAO

# Test warehouse operations
java DepoDAO
```

### Integration Testing
- Test complete user workflows
- Verify database transactions
- Test transfer operations
- Validate reporting functionality

## 📊 Performance Considerations

- **Database Indexing**: Proper indexes on frequently queried columns
- **Connection Pooling**: Efficient database connection management
- **Lazy Loading**: Load data on demand to improve performance
- **Caching**: Cache frequently accessed data
- **Batch Operations**: Optimize bulk operations

## 🔒 Security Features

- **Password Encryption**: Secure password storage
- **Session Management**: Secure user sessions
- **Role-Based Access Control**: Granular permissions
- **Input Validation**: Prevent SQL injection and XSS
- **Audit Trail**: Complete operation logging

## 🚀 Future Enhancements

- [ ] **Web Interface**: Browser-based access
- [ ] **Mobile App**: Android/iOS mobile applications
- [ ] **Barcode Scanning**: QR/Barcode integration
- [ ] **REST API**: API for third-party integrations
- [ ] **Advanced Analytics**: Business intelligence features
- [ ] **Multi-Language Support**: Internationalization
- [ ] **Cloud Deployment**: Cloud-based hosting options

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines
- Follow Java coding conventions
- Write comprehensive javadoc comments
- Include unit tests for new features
- Update documentation for API changes

## 📚 Documentation

- **UML Diagram**: See `uml.jpg` for system architecture overview
- **API Documentation**: Javadoc comments in source code
- **Database Documentation**: SQL schema and relationships
- **User Manual**: Step-by-step usage instructions

## 🐛 Troubleshooting

### Common Issues

**Database Connection Failed**
```bash
# Check MySQL service status
sudo systemctl status mysql

# Verify credentials in DatabaseConnection.java
# Ensure MySQL JDBC driver is in classpath
```

**GUI Not Displaying Correctly**
```bash
# Check Java Swing look and feel settings
# Verify screen resolution compatibility
# Update Java to latest version
```

**Performance Issues**
```bash
# Monitor database query performance
# Check memory usage with Java profiler
# Optimize database indexes
```

## 📝 License

This project is developed for educational and commercial purposes. Please refer to the license file for detailed terms and conditions.

## 👥 Support

For support and questions:
- Create an issue on GitHub
- Check the documentation
- Review the UML diagram for system understanding

---

**Note**: This warehouse tracking system is designed for enterprise-level inventory management and provides a solid foundation for supply chain automation.

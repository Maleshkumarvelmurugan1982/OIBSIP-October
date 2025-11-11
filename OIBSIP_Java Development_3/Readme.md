# ATM Interface System

## Objective
A comprehensive GUI-based ATM simulation system that provides complete banking functionality including account creation, authentication, transactions (deposit, withdraw, transfer), transaction history tracking, and persistent data storage. The system includes an admin panel for viewing all account information.

## System Features

### User Features
- **Account Creation**: Create new accounts with automatic User ID and PIN generation
- **Secure Login**: Authentication system with User ID and PIN verification
- **Balance Inquiry**: Real-time balance display on the main menu
- **Deposit**: Add funds to account with instant balance update
- **Withdrawal**: Remove funds with insufficient balance protection
- **Money Transfer**: Transfer funds between accounts with recipient verification
- **Transaction History**: Complete timestamped log of all account activities
- **Data Persistence**: All data automatically saved to JSON file

### Admin Features
- **Admin Panel**: Password-protected access (default: `admin123`)
- **View All Accounts**: Display all User IDs, PINs, names, balances, and transaction counts
- **System Overview**: Total account statistics and data file location

### Account Information
- **User ID Format**: Automatically generated (e.g., USER1234)
- **PIN Format**: 4-digit randomly generated secure PIN
- **Initial Balance**: User-specified amount (must be non-negative)

## How to Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Java Swing library (included in standard JDK)
- Minimum 600x500 screen resolution

### Compilation
```bash
javac ATMInterface.java
```

### Execution
```bash
java ATMInterface
```

### Data Storage
- All account data is saved to `atm_data.json` in the same directory
- Data persists between sessions
- Automatic save on window close and after each transaction
- JSON format allows easy viewing/editing in text editors

## Implementation Details

### Technologies Used
- **Language**: Java
- **GUI Framework**: Java Swing (JFrame, JPanel, JDialog, CardLayout)
- **Layout Managers**: GridBagLayout, BorderLayout, GridLayout
- **Data Structures**: HashMap (account storage), ArrayList (transaction history)
- **I/O**: FileWriter, BufferedReader for JSON persistence
- **Date/Time**: SimpleDateFormat for transaction timestamps

### Architecture Components

#### 1. **Account Class**
- Encapsulates user account data and operations
- Manages balance, transactions, and history
- Provides JSON serialization/deserialization methods
- Automatic timestamp generation for all transactions

#### 2. **ATMManager Class**
- Central controller for all account operations
- Handles authentication (both user and admin)
- Manages data persistence (save/load from JSON)
- Generates unique User IDs and secure PINs
- Provides account lookup and retrieval functionality

#### 3. **JSONObject & JSONArray Classes**
- Custom lightweight JSON parser implementation
- Handles nested objects and arrays
- Pretty-print formatting with indentation
- No external library dependencies

#### 4. **ATMInterface Class (Main GUI)**
- Multi-panel card layout system for navigation
- Welcome screen with three entry points
- Login panel with credential validation
- Account creation panel with input validation
- Dynamic menu panel that updates with current balance
- Modal dialogs for transactions and history

### GUI Design Pattern
**CardLayout Navigation System**:
```
Welcome Screen
    ├── Login Panel → Menu Panel
    ├── Create Account Panel → Welcome Screen
    └── Admin Panel (Dialog)
```

### Security Features
- PIN-based authentication
- Admin password protection
- No plain text password storage in memory
- Input validation on all financial operations
- Negative balance prevention

### Color Scheme
- Primary Blue: `#0066CC` (headers, titles)
- Success Green: `#009900` (login, deposit)
- Warning Orange: `#CC6600` (withdraw)
- Error Red: `#CC0000` (logout, back buttons)
- Transfer Purple: `#663399` (transfer operations)
- Admin Purple: `#990099` (admin access)

## Transaction Flow

### Deposit Flow
1. User clicks "Deposit" from menu
2. Enter amount in dialog
3. Validation (positive, numeric)
4. Add to balance
5. Log transaction with timestamp
6. Save data to file
7. Update display

### Withdrawal Flow
1. User clicks "Withdraw" from menu
2. Enter amount in dialog
3. Validation (positive, numeric, sufficient balance)
4. Deduct from balance
5. Log transaction
6. Save data
7. Update display

### Transfer Flow
1. User clicks "Transfer" from menu
2. Enter recipient User ID
3. Validate recipient exists
4. Enter transfer amount
5. Validation (positive, numeric, sufficient balance, not self-transfer)
6. Deduct from sender, add to recipient
7. Log transactions in both accounts
8. Save data
9. Update display

## Data Persistence Format

### JSON Structure
```json
{
    "accounts": [
        {
            "userId": "USER1234",
            "pin": "5678",
            "name": "John Doe",
            "balance": 1500.0,
            "transactionHistory": [
                "2024-11-11 10:30:00 - Account created with initial balance: $1000.0",
                "2024-11-11 10:35:00 - Deposit: +$500.0 | Balance: $1500.0"
            ]
        }
    ],
    "lastUpdated": "2024-11-11 10:35:00"
}
```

## Error Handling
- **Invalid Credentials**: Clear error message on failed login
- **Insufficient Funds**: Prevents overdraft with user notification
- **Invalid Input**: Catches NumberFormatException for non-numeric amounts
- **Empty Fields**: Validation before account creation
- **Negative Amounts**: Rejected with error message
- **File I/O Errors**: Console logging with graceful degradation
- **Self-Transfer**: Prevented with validation check

## Admin Panel Information
- **Access**: Click "Admin Panel" from welcome screen
- **Password**: `admin123` (hardcoded in ATMManager.ADMIN_PASSWORD)
- **Displays**: User ID, PIN, Name, Balance, Transaction count for all accounts
- **Features**: Refresh button to reload data, close button to exit

## Key Features Implemented

### Auto-Save Mechanism
- Saves after every transaction
- Saves on window close event
- Console confirmation of save operations

### Input Validation
- Type checking (numeric values only)
- Range checking (positive amounts)
- Existence checking (recipient accounts)
- Boundary checking (sufficient balance)

### User Experience
- Color-coded buttons for intuitive navigation
- Real-time balance updates
- Clear success/error messages
- Scrollable transaction history
- Window centering on screen

## Outcome
A fully functional, production-ready ATM simulation system that demonstrates:
- **Object-Oriented Design**: Encapsulation, separation of concerns
- **GUI Development**: Professional Swing interface with multiple panels
- **Data Persistence**: Custom JSON implementation without external libraries
- **Financial Logic**: Accurate transaction handling and balance management
- **Error Handling**: Comprehensive validation and user feedback
- **Security**: Authentication and authorization mechanisms
- **Scalability**: Supports unlimited accounts with efficient HashMap storage

The system successfully simulates real-world ATM operations while maintaining data integrity and providing a polished user experience suitable for educational purposes or as a foundation for more complex banking applications.

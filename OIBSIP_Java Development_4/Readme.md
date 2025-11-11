# Online Examination System

## Objective
A comprehensive GUI-based online examination platform built with Java Swing that enables users to register, take timed Java programming tests at multiple difficulty levels, track their exam history, and manage their profiles. The system includes persistent data storage, automatic grading, and performance analytics.

## System Features

### User Management
- **User Registration**: Create account with username, full name, email, and password
- **Secure Login**: Authentication with username/password verification
- **Profile Management**: Update full name and email information
- **Password Management**: Change password with current password verification
- **Data Persistence**: All user data saved to `exam_users.txt` file

### Examination Features
- **Three Difficulty Levels**:
  - **Easy**: 15 questions available, 10-question exams
  - **Medium**: 30 questions available, 25-question exams  
  - **Hard**: 50 questions available, 50 or 100-question exams
- **Timed Exams**: 1 minute per question (auto-submit when time expires)
- **Question Navigation**: Move backward/forward through questions
- **Answer Tracking**: Save answers and review before submission
- **Random Selection**: Questions randomly shuffled from pool
- **Instant Grading**: Automatic scoring upon submission

### Question Bank
- **Easy Topics**: Basic Java syntax, data types, keywords, simple concepts
- **Medium Topics**: Collections, OOP principles, exception handling, multithreading basics
- **Hard Topics**: Advanced concurrency, design patterns, Spring Framework, Java 8+ features, performance optimization

### Analytics & History
- **Exam History**: Complete record of all attempts with dates
- **Performance Tracking**: Score, percentage, pass/fail status
- **Detailed Results**: Question count, correct/wrong answers, percentage
- **Pass Threshold**: 70% required to pass

## How to Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Java Swing library (included in standard JDK)
- Minimum 900x600 screen resolution for exam interface

### Compilation
```bash
javac OnlineExamSystem.java
```

### Execution
```bash
java OnlineExamSystem
```

### Data Storage
- User data stored in `exam_users.txt` in the same directory
- Format: Pipe-separated values with exam history
- Automatic save after registration, profile updates, and exam completion
- Persistent across sessions

## Implementation Details

### Technologies Used
- **Language**: Java (JDK 8+)
- **GUI Framework**: Java Swing
  - JFrame (main windows)
  - JDialog (modal dialogs)
  - JPanel with various layouts
  - JButton, JTextField, JPasswordField, JRadioButton, JLabel
- **Layout Managers**: 
  - BorderLayout (main structure)
  - GridBagLayout (forms)
  - GridLayout (options, buttons)
  - FlowLayout (navigation)
- **Data Structures**:
  - HashMap (user storage)
  - ArrayList (question pools, exam history)
  - Arrays (question options, user answers)
- **Date/Time**: Java 8+ `LocalDateTime` and `DateTimeFormatter`
- **I/O**: BufferedReader/BufferedWriter for file operations
- **Concurrency**: javax.swing.Timer for exam countdown

### Architecture Components

#### 1. **User Class**
- Encapsulates user credentials and profile information
- Maintains exam history as list of ExamResult objects
- Getter/setter methods for profile updates

#### 2. **ExamResult Class**
- Stores individual exam attempt data
- Calculates percentage automatically
- Timestamp generation using LocalDateTime
- toString() method for display formatting

#### 3. **Question Class**
- Stores question text, four options, and correct answer index
- Immutable design (only getters)
- Generic structure supports any multiple-choice question

#### 4. **QuestionBank Class**
- Static repository of all questions
- Three separate pools for each difficulty level
- Random selection and shuffling for each exam
- Comprehensive Java programming topics covered

#### 5. **UserManager Class**
- Singleton-like static manager for all user operations
- Handles authentication and registration
- File I/O for persistence
- Custom parsing for pipe-separated format

#### 6. **GUI Classes**
- **LoginFrame**: Entry point with login/register options
- **RegisterFrame**: Modal dialog for new user registration
- **DashboardFrame**: Main menu after login (5 options)
- **ExamFrame**: Full examination interface with timer

### User Interface Design

#### Color Scheme
- Background: Light Blue (`#F0F8FF` - Alice Blue)
- Headers: Deep Blue (`#0066CC`)
- Success/Login: Green
- Danger/Logout: Red
- Caution/Warning: Orange
- Info: Magenta/Pink
- Navigation: Blue/Gray

#### Navigation Flow
```
LoginFrame
    ├── Login → DashboardFrame
    └── Register → RegisterFrame → LoginFrame

DashboardFrame
    ├── Start Exam → Select Difficulty → ExamFrame → Results → DashboardFrame
    ├── View History (Modal Dialog)
    ├── Update Profile (Modal Dialog)
    ├── Change Password (Modal Dialog)
    └── Logout → LoginFrame
```

## Examination Workflow

### 1. Exam Setup
1. User selects difficulty from dashboard
2. Questions randomly selected from pool
3. Timer initialized (1 minute × question count)
4. User answers array initialized to -1 (unanswered)

### 2. During Exam
1. Display question with 4 radio button options
2. User selects answer (saved immediately)
3. Navigate using Previous/Next buttons
4. Timer counts down, turns red at 60 seconds
5. Visual indicators: question number, time remaining

### 3. Exam Submission
1. Check for unanswered questions (confirmation if any)
2. Stop timer
3. Calculate score by comparing with correct answers
4. Create ExamResult object with timestamp
5. Save to user's exam history
6. Display results modal
7. Return to dashboard

### 4. Auto-Submit
- Triggers when timer reaches 0
- Warning message shown to user
- Proceeds with normal submission workflow

## Data Persistence Format

### File Structure (`exam_users.txt`)
```
USER:username|password|fullName|email
HISTORY:difficulty,totalQuestions,correctAnswers,date;difficulty,totalQuestions,correctAnswers,date
USER:username2|password2|fullName2|email2
HISTORY:
```

### Example
```
USER:john_doe|pass123|John Doe|john@example.com
HISTORY:Easy,10,8,2024-11-11 10:30:00;Medium,25,20,2024-11-11 11:45:00
USER:jane_smith|secure456|Jane Smith|jane@example.com
HISTORY:Hard,50,42,2024-11-11 09:15:00
```

## Scoring System

### Calculation
```
Correct Answers = Count of matching user answers with correct answers
Percentage = (Correct Answers / Total Questions) × 100
Pass/Fail = Percentage >= 70% ? PASS : FAIL
```

### Result Display
- Total Questions
- Correct Answers
- Wrong Answers (Total - Correct)
- Percentage (2 decimal places)
- Pass/Fail status (color-coded: green/red)

## Security Features

### Authentication
- Password verification during login
- Current password required for password changes
- Username uniqueness enforced during registration

### Input Validation
- Empty field checking on all forms
- Password confirmation matching
- Email format (basic validation via UI)
- Trim whitespace from inputs

### Exam Integrity
- Window closing disabled during exam
- Warning message if user attempts to close
- Must submit exam to exit
- No navigation to other screens during exam

## Error Handling

### Registration
- Username already exists → Error message
- Empty fields → Validation error
- Password mismatch → Confirmation error

### Login
- Invalid credentials → Error message
- Empty fields → Validation error

### Profile Updates
- Empty fields ignored/rejected
- Validation before save

### Exam
- Unanswered questions → Confirmation dialog
- Time expiry → Auto-submit with warning
- Prevent window closing → Warning message

## Question Bank Statistics

### Easy Level
- **Total Questions**: 15
- **Topics**: Java basics, syntax, primitive types, keywords, simple concepts
- **Difficulty**: Beginner-friendly, fundamental knowledge

### Medium Level
- **Total Questions**: 30
- **Topics**: Collections, OOP, exception handling, threads, access modifiers, polymorphism
- **Difficulty**: Intermediate knowledge, requires understanding of core Java

### Hard Level
- **Total Questions**: 50
- **Topics**: Advanced concurrency, design patterns, Spring Framework, Java 8+, streams, functional interfaces, performance optimization
- **Difficulty**: Advanced knowledge, real-world scenarios

## Key Features Implemented

### Timer Management
- Real-time countdown display
- Color change warning (red at 60 seconds)
- Auto-submit on timeout
- Pause not allowed (exam integrity)

### Navigation System
- Previous/Next buttons
- Enable/disable based on position
- Answer preservation across navigation
- Submit button always available

### Result Analytics
- Instant calculation
- Historical tracking
- HTML-formatted display
- Color-coded pass/fail

### User Experience
- System look-and-feel for native appearance
- Hand cursor on clickable buttons
- Focus painting disabled for cleaner UI
- Consistent color scheme throughout
- Modal dialogs for important actions
- Centered windows on screen
- Responsive button sizing

## Exam History Display

### History View
- Table format with HTML rendering
- Columns: Date, Level, Score, Percentage
- Chronological order (oldest to newest)
- Accessible from dashboard
- Read-only view

### Empty History Handling
- Graceful message when no exams taken
- Encourages user to start first exam

## Profile Management

### Updatable Fields
- Full Name
- Email Address

### Non-Updatable Fields
- Username (unique identifier)
- Password (separate change password function)

### Update Workflow
1. Click "Update Profile"
2. Pre-filled dialog with current values
3. Modify desired fields
4. Confirm to save
5. Immediate effect on dashboard display

## Password Management

### Change Password Workflow
1. Enter current password (verification)
2. Enter new password
3. Confirm new password
4. Validation checks
5. Update and save

### Security Checks
- Current password must match
- New password cannot be empty
- New password and confirmation must match

## Outcome

A fully functional, production-ready online examination system that demonstrates:

### Technical Achievements
- **Object-Oriented Design**: Clear separation of concerns with distinct classes
- **GUI Development**: Professional multi-window Swing application
- **State Management**: Proper handling of user sessions and exam state
- **Data Persistence**: Custom file-based storage without external databases
- **Timer Implementation**: Real-time countdown with automatic actions
- **Input Validation**: Comprehensive error handling and user feedback
- **Navigation Logic**: Complex state transitions between screens

### Educational Value
- Comprehensive Java question bank covering beginner to advanced topics
- Immediate feedback for learning reinforcement
- Historical tracking for progress monitoring
- Multiple difficulty levels for skill progression

### User Experience
- Intuitive interface with clear navigation
- Visual feedback (colors, timers, status)
- Modal dialogs for important decisions
- Responsive design with proper layouts
- Professional appearance with consistent styling

### Real-World Applications
Suitable for:
- Educational institutions (programming courses)
- Technical training programs
- Self-assessment tools for Java learners
- Interview preparation platforms
- Certification practice systems

The system successfully simulates a complete online examination platform while maintaining code quality, user experience, and data integrity suitable for academic projects or as a foundation for commercial examination systems.

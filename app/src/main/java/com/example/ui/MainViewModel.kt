package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen {
    object Dashboard : Screen()
    object StudentHub : Screen()
    object DigitalVault : Screen()
    object IdentityVault : Screen()
    object CareerHub : Screen()
    object FinanceHub : Screen()
    object AssetManager : Screen()
    object FamilyHub : Screen()
    object CommunityHub : Screen()
    object ServiceMarketplace : Screen()
    object AdminPanel : Screen()
    object Profile : Screen()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // 1. DATABASE SETUP
    private val db = Room.databaseBuilder(
        application,
        LifeHubDatabase::class.java,
        "lifehub_database"
    ).fallbackToDestructiveMigration().build()

    private val dao = db.dao()

    // 2. STATE FLOWS
    val currentScreen: MutableStateFlow<Screen> = MutableStateFlow(Screen.Dashboard)

    // User Profile Tier State
    val isPremium: MutableStateFlow<Boolean> = MutableStateFlow(false)

    // Student Hub States
    val notes: StateFlow<List<Note>> = dao.getAllNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val assignments: StateFlow<List<Assignment>> = dao.getAllAssignments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val attendanceList: StateFlow<List<Attendance>> = dao.getAllAttendance()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val timetables: StateFlow<List<Timetable>> = dao.getAllTimetables()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val exams: StateFlow<List<Exam>> = dao.getAllExams()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val internships: StateFlow<List<Internship>> = dao.getAllInternships()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val studyMaterials: StateFlow<List<StudyMaterial>> = dao.getAllStudyMaterials()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Digital & Identity Vault States
    val documents: StateFlow<List<Document>> = dao.getAllDocuments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val identities: StateFlow<List<IdentityCard>> = dao.getAllIdentities()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Career Hub States
    val careerProfile: StateFlow<CareerProfile?> = dao.getCareerProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Finance States
    val financeItems: StateFlow<List<FinanceItem>> = dao.getAllFinanceItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Asset Manager States
    val assets: StateFlow<List<Asset>> = dao.getAllAssets()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Family States
    val familyMembers: StateFlow<List<FamilyMember>> = dao.getAllFamilyMembers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Community States
    val communityListings: StateFlow<List<CommunityListing>> = dao.getAllCommunityListings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Service Bookings States
    val serviceBookings: StateFlow<List<ServiceBooking>> = dao.getAllServiceBookings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // AI States
    private val _aiResult = MutableStateFlow<String?>(null)
    val aiResult: StateFlow<String?> = _aiResult.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    // 3. SEEDING / FIRST START PREPARATION
    init {
        viewModelScope.launch {
            // Seed sample database if empty
            notes.first().let {
                if (it.isEmpty()) {
                    seedSampleData()
                }
            }
        }
    }

    private suspend fun seedSampleData() {
        // Sample Notes
        dao.insertNote(Note(title = "Calculus II Revision", content = "Focus on integration by parts, trigonometric substitution, and Taylor Series approximations.", category = "Academics"))
        dao.insertNote(Note(title = "Startup Pitch Material", content = "Make sure the problem statement is bold and clear. Introduce Life Operating System features early on.", category = "Career"))
        
        // Sample Assignments
        dao.insertAssignment(Assignment(title = "Database Systems Proposal", subject = "CS402", dueDate = "2026-06-25", status = "Pending", priority = "High"))
        dao.insertAssignment(Assignment(title = "Algorithm Design Home Assignment", subject = "CS301", dueDate = "2026-06-22", status = "Completed", priority = "Medium"))

        // Sample Attendance
        dao.insertAttendance(Attendance(subject = "Computer Networks", attended = 24, total = 28))
        dao.insertAttendance(Attendance(subject = "Software Architecture", attended = 18, total = 20))
        dao.insertAttendance(Attendance(subject = "Economics for Managers", attended = 10, total = 15))

        // Sample Timetable
        dao.insertTimetable(Timetable(dayOfWeek = "Monday", time = "09:00 AM - 10:30 AM", subject = "Computer Networks", venue = "Room 403"))
        dao.insertTimetable(Timetable(dayOfWeek = "Monday", time = "11:00 AM - 12:30 PM", subject = "Software Architecture", venue = "Lab 1"))
        dao.insertTimetable(Timetable(dayOfWeek = "Wednesday", time = "02:00 PM - 03:30 PM", subject = "Economics", venue = "Seminar Hall"))

        // Sample Exams
        dao.insertExam(Exam(subject = "Compiler Design Midterm", date = "2026-07-05", time = "10:00 AM", venue = "Hall B"))
        dao.insertExam(Exam(subject = "Mobile Computing Final", date = "2026-07-12", time = "02:00 PM", venue = "Labs Compound"))

        // Sample Internships
        dao.insertInternship(Internship(title = "Software Engineer Intern", company = "Google Cloud", role = "Kotlin & Jetpack Compose Developer", stipend = "$3,500/Mo", status = "Shortlisted", description = "Build premium dashboard applications for internal developer operations testing and client feedback."))
        
        // Sample Study Materials
        dao.insertStudyMaterial(StudyMaterial(title = "Algorithm Design Manual Part I", category = "EBook", description = "Standard guidebook for fundamental and advanced algorithms, dynamic programming, and graphs."))

        // Sample Docs
        dao.insertDocument(Document(name = "University_Transcript_Sem4.pdf", size = "1.8 MB", category = "Certificates"))
        dao.insertDocument(Document(name = "Premium_Resume_LifeHub.pdf", size = "420 KB", category = "Resumes"))

        // Sample Identities
        dao.insertIdentity(IdentityCard(cardType = "Aadhaar Card", holderName = "Jane Doe", cardNumber = "XXXX-XXXX-8923", expiryDate = "N/A", secureNote = "Primary identification proof. Retrace carefully."))
        dao.insertIdentity(IdentityCard(cardType = "PAN Card", holderName = "Jane Doe", cardNumber = "XXXXX9011X", expiryDate = "N/A", secureNote = "Tax identification and finance validation card."))

        // Sample Career Profile
        dao.insertCareerProfile(CareerProfile(
            fullName = "Jane Doe",
            email = "jane.doe@lifehub.io",
            phone = "+1 555-019-2831",
            bio = "Ambitious computer science senior and mobile designer focused on clean Kotlin architectures and Material 3 design systems.",
            skills = "Kotlin, Jetpack Compose, UX Design, SQL Database Architecture, Next.js, TypeScript",
            achievements = "1st Place Mobile Hackathon 2025\nMaintained 3.92 CGPA through 4 semesters\nOpen-source contributor to jetpack navigation UI components",
            education = "B.Tech in Computer Science and Engineering, Tech-State University (Expected 2027)"
        ))

        // Sample Finance
        dao.insertFinanceItem(FinanceItem(amount = 120.0, category = "Rent", date = "2026-06-15", description = "Monthly Dorm Fees", type = "Expense"))
        dao.insertFinanceItem(FinanceItem(amount = 14.99, category = "Entertainment", date = "2026-06-17", description = "Spotify Premium Sub", type = "Subscription", reminderDate = "25th Every Month"))
        dao.insertFinanceItem(FinanceItem(amount = 50.0, category = "Tutoring", date = "2026-06-18", description = "Math Tutoring Income", type = "Income"))
        dao.insertFinanceItem(FinanceItem(amount = 35.0, category = "Electricity Bill", date = "2026-06-19", description = "Hostel Utilities", type = "Bill", reminderDate = "2026-06-28"))

        // Sample Assets
        dao.insertAsset(Asset(name = "MacBook Pro 16", category = "Electronics", purchaseDate = "2025-08-10", value = 2499.0, warrantyExpiry = "2026-08-10", serialNumber = "C02XYZ123ABC"))
        dao.insertAsset(Asset(name = "Honda Activa Scooter", category = "Vehicles", purchaseDate = "2024-03-05", value = 1100.0, warrantyExpiry = "2029-03-05", serialNumber = "MD3JA123XXXXXXXX"))

        // Sample Family Elements
        dao.insertFamilyMember(FamilyMember(name = "Robert Doe", relationship = "Father", contact = "+1 555-012-4432", medicines = "Aspirin 81mg (Daily morning)", appointments = "Cardiologist Review (July 10, 2026)", emergencyContact = true))
        dao.insertFamilyMember(FamilyMember(name = "Sarah Doe", relationship = "Mother", contact = "+1 555-012-4433", medicines = "Multivitamin (Daily evening)", appointments = "Dental Cleaning (July 24, 2026)", emergencyContact = true))

        // Sample Communities
        dao.insertCommunityListing(CommunityListing(title = "Scientific Calculator Casio fx-991EX", description = "Barely used Casio engineering calculator. Perfect for advanced math exams and complex integration problems.", price = "$20", contact = "+1 555-220-1100", section = "BuySell", dateAdded = "2 days ago"))
        dao.insertCommunityListing(CommunityListing(title = "Lost Room Keys near Cafeteria", description = "Silver keychain with a small leather tag. Please contact me immediately if found near campus ground.", price = "Reward $10", contact = "+1 555-220-1202", section = "LostFound", dateAdded = "Just now"))

        // Sample Service Marketplace
        dao.insertServiceBooking(ServiceBooking(providerName = "Prof. Alexander Smith", serviceCategory = "Tutors", date = "2026-06-22", cost = "$45/hr", status = "Confirmed", rating = 5))
        dao.insertServiceBooking(ServiceBooking(providerName = "Marcus Vance (Creative Visuals)", serviceCategory = "Photographers", date = "2026-06-29", cost = "$150/Session", status = "Pending", rating = 4))
    }

    // 4. DATABASE TRANSACTIONS / OPERATIONS

    // Notes
    fun addNote(title: String, content: String, category: String) {
        viewModelScope.launch {
            dao.insertNote(Note(title = title, content = content, category = category))
        }
    }
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            dao.deleteNote(note)
        }
    }

    // Assignments
    fun addAssignment(title: String, subject: String, dueDate: String, priority: String) {
        viewModelScope.launch {
            dao.insertAssignment(Assignment(title = title, subject = subject, dueDate = dueDate, status = "Pending", priority = priority))
        }
    }
    fun updateAssignmentStatus(assignment: Assignment, completed: Boolean) {
        viewModelScope.launch {
            dao.insertAssignment(assignment.copy(status = if (completed) "Completed" else "Pending"))
        }
    }
    fun deleteAssignment(assignment: Assignment) {
        viewModelScope.launch {
            dao.deleteAssignment(assignment)
        }
    }

    // Attendance
    fun addAttendance(subject: String, attended: Int, total: Int) {
        viewModelScope.launch {
            dao.insertAttendance(Attendance(subject = subject, attended = attended, total = total))
        }
    }
    fun updateAttendanceCount(attendance: Attendance, incrementAttended: Boolean) {
        viewModelScope.launch {
            val att = if (incrementAttended) {
                attendance.copy(attended = attendance.attended + 1, total = attendance.total + 1)
            } else {
                attendance.copy(total = attendance.total + 1)
            }
            dao.insertAttendance(att)
        }
    }
    fun deleteAttendance(attendance: Attendance) {
        viewModelScope.launch {
            dao.deleteAttendance(attendance)
        }
    }

    // Timetable & Exams
    fun addTimetable(day: String, time: String, subject: String, venue: String) {
        viewModelScope.launch {
            dao.insertTimetable(Timetable(dayOfWeek = day, time = time, subject = subject, venue = venue))
        }
    }
    fun deleteTimetable(timetable: Timetable) {
        viewModelScope.launch {
            dao.deleteTimetable(timetable)
        }
    }

    fun addExam(subject: String, date: String, time: String, venue: String) {
        viewModelScope.launch {
            dao.insertExam(Exam(subject = subject, date = date, time = time, venue = venue))
        }
    }
    fun deleteExam(exam: Exam) {
        viewModelScope.launch {
            dao.deleteExam(exam)
        }
    }

    // Documents
    fun addDocument(name: String, size: String, category: String) {
        viewModelScope.launch {
            dao.insertDocument(Document(name = name, size = size, category = category))
        }
    }
    fun deleteDocument(document: Document) {
        viewModelScope.launch {
            dao.deleteDocument(document)
        }
    }

    // Identities
    fun addIdentity(cardType: String, holderName: String, cardNumber: String, expiryDate: String, note: String) {
        viewModelScope.launch {
            dao.insertIdentity(IdentityCard(cardType = cardType, holderName = holderName, cardNumber = cardNumber, expiryDate = expiryDate, secureNote = note))
        }
    }
    fun deleteIdentity(card: IdentityCard) {
        viewModelScope.launch {
            dao.deleteIdentity(card)
        }
    }

    // Career Profile
    fun saveCareerProfile(fullName: String, email: String, phone: String, bio: String, skills: String, achievements: String, education: String) {
        viewModelScope.launch {
            dao.insertCareerProfile(CareerProfile(
                fullName = fullName,
                email = email,
                phone = phone,
                bio = bio,
                skills = skills,
                achievements = achievements,
                education = education
            ))
        }
    }

    // Finance
    fun addFinanceItem(amount: Double, category: String, date: String, description: String, type: String, reminderDate: String = "") {
        viewModelScope.launch {
            dao.insertFinanceItem(FinanceItem(amount = amount, category = category, date = date, description = description, type = type, reminderDate = reminderDate))
        }
    }
    fun toggleBillPaid(item: FinanceItem) {
        viewModelScope.launch {
            dao.insertFinanceItem(item.copy(isPaid = !item.isPaid))
        }
    }
    fun deleteFinanceItem(item: FinanceItem) {
        viewModelScope.launch {
            dao.deleteFinanceItem(item)
        }
    }

    // Assets
    fun addAsset(name: String, category: String, value: Double, purchaseDate: String, warrantyExpiry: String, serialNumber: String) {
        viewModelScope.launch {
            dao.insertAsset(Asset(name = name, category = category, value = value, purchaseDate = purchaseDate, warrantyExpiry = warrantyExpiry, serialNumber = serialNumber))
        }
    }
    fun deleteAsset(asset: Asset) {
        viewModelScope.launch {
            dao.deleteAsset(asset)
        }
    }

    // Family
    fun addFamily(name: String, relationship: String, contact: String, medicines: String, appointments: String, emergencyContact: Boolean) {
        viewModelScope.launch {
            dao.insertFamilyMember(FamilyMember(name = name, relationship = relationship, contact = contact, medicines = medicines, appointments = appointments, emergencyContact = emergencyContact))
        }
    }
    fun deleteFamily(member: FamilyMember) {
        viewModelScope.launch {
            dao.deleteFamilyMember(member)
        }
    }

    // Community
    fun addCommunityListing(title: String, description: String, price: String, contact: String, section: String) {
        viewModelScope.launch {
            dao.insertCommunityListing(CommunityListing(title = title, description = description, price = price, contact = contact, section = section, dateAdded = "Just now"))
        }
    }
    fun deleteCommunityListing(listing: CommunityListing) {
        viewModelScope.launch {
            dao.deleteCommunityListing(listing)
        }
    }

    // Services
    fun addServiceBooking(providerName: String, category: String, date: String, cost: String) {
        viewModelScope.launch {
            dao.insertServiceBooking(ServiceBooking(providerName = providerName, serviceCategory = category, date = date, cost = cost, status = "Pending"))
        }
    }
    fun deleteServiceBooking(booking: ServiceBooking) {
        viewModelScope.launch {
            dao.deleteServiceBooking(booking)
        }
    }

    // Global toggle premium
    fun togglePremium() {
        isPremium.update { !it }
    }

    // Clear AI output
    fun clearAiOutput() {
        _aiResult.value = null
    }

    // 5. AI LOGIC (GEMINI API INTERACTIONS)

    // Note summarization (AI Notes Summaries — Premium feature or normal)
    fun summarizeNote(noteTitle: String, noteContent: String) {
        viewModelScope.launch {
            _isAiLoading.value = true
            _aiResult.value = null
            
            val systemPrompt = "You are a professional life organizer AI, specializing in clean summaries. Summarize the user's note with 3 actionable, bulleted insights, highlighting the key takeaway."
            val userPrompt = "Note Title: $noteTitle\nNote Content: $noteContent\n\nPlease provide a clear, professional visual summary."
            
            val response = GeminiClient.generate(userPrompt, systemPrompt)
            _aiResult.value = response
            _isAiLoading.value = false
        }
    }

    // AI Resume Summary & Optimization (AI Resume Builder)
    fun buildAiResumeOptimize(fullName: String, bio: String, skills: String, achievements: String) {
        viewModelScope.launch {
            _isAiLoading.value = true
            _aiResult.value = null
            
            val systemPrompt = "You are an elite Silicon Valley executive resume builder. Format the applicant's experience and bio into a premium, eye-catching 'Professional Summary' and an optimized 'Achievements' list to grab recruiter attention immediately."
            val userPrompt = """
                Applicant: $fullName
                Bio/Objective: $bio
                Key Stack / Skills: $skills
                Key Achievements:
                $achievements
                
                Please generate a world-class professional summary layout. Make it elegant and professional.
            """.trimIndent()
            
            val response = GeminiClient.generate(userPrompt, systemPrompt)
            _aiResult.value = response
            _isAiLoading.value = false
        }
    }
}

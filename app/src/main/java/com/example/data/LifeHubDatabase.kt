package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ==========================================
// MODULE 1 — STUDENT HUB ENTITIES
// ==========================================

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val category: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "assignments")
data class Assignment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val subject: String,
    val dueDate: String,
    val status: String, // "Pending", "Completed"
    val priority: String // "High", "Medium", "Low"
)

@Entity(tableName = "attendance")
data class Attendance(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subject: String,
    val attended: Int,
    val total: Int
)

@Entity(tableName = "timetables")
data class Timetable(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dayOfWeek: String, // "Monday", "Tuesday", etc.
    val time: String,
    val subject: String,
    val venue: String
)

@Entity(tableName = "exams")
data class Exam(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subject: String,
    val date: String,
    val time: String,
    val venue: String
)

@Entity(tableName = "internships")
data class Internship(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val company: String,
    val role: String,
    val stipend: String,
    val status: String, // "Applied", "Saved", "Shortlisted"
    val description: String
)

@Entity(tableName = "study_materials")
data class StudyMaterial(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // "Notes", "Papers", "EBook"
    val description: String,
    val downloadUrl: String = ""
)

// ==========================================
// MODULE 2 & 3 — DIGITAL & IDENTITY VAULT ENTITIES
// ==========================================

@Entity(tableName = "documents")
data class Document(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val size: String,
    val category: String, // "Certificates", "PDFs", "Resumes", "Portfolios", "Images"
    val dateAdded: Long = System.currentTimeMillis(),
    val filepath: String = ""
)

@Entity(tableName = "identities")
data class IdentityCard(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cardType: String, // "Aadhaar", "PAN", "Passport", "Driving License", "Insurance", "College ID"
    val holderName: String,
    val cardNumber: String, // Securely encrypted/masked in live view
    val expiryDate: String,
    val secureNote: String,
    val isEncrypted: Boolean = true
)

// ==========================================
// MODULE 4 — CAREER HUB ENTITIES
// ==========================================

@Entity(tableName = "career_profiles")
data class CareerProfile(
    @PrimaryKey val id: Long = 1, // Single profile
    val fullName: String,
    val email: String,
    val phone: String,
    val bio: String,
    val skills: String, // Comma separated list
    val achievements: String, // Newline separated list
    val education: String
)

// ==========================================
// MODULE 5 — FINANCE HUB ENTITIES
// ==========================================

@Entity(tableName = "finance_items")
data class FinanceItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val category: String, // "Food", "Rent", "Fees", "Sub", "EMI", etc.
    val date: String,
    val description: String,
    val type: String, // "Expense", "Income", "Subscription", "Bill", "EMI"
    val reminderDate: String = "", // Used for subscriptions and bills
    val isPaid: Boolean = false
)

// ==========================================
// MODULE 6 — ASSET MANAGER ENTITIES
// ==========================================

@Entity(tableName = "assets")
data class Asset(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String, // "Electronics", "Vehicles", "Appliances"
    val purchaseDate: String,
    val value: Double,
    val warrantyExpiry: String,
    val serialNumber: String = ""
)

// ==========================================
// MODULE 7 — FAMILY HUB ENTITIES
// ==========================================

@Entity(tableName = "family_members")
data class FamilyMember(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val relationship: String, // "Father", "Mother", etc.
    val contact: String,
    val medicines: String, // Medicine reminders
    val appointments: String, // Upcoming checkup reminders
    val emergencyContact: Boolean = false
)

// ==========================================
// MODULE 8 — COMMUNITY HUB ENTITIES
// ==========================================

@Entity(tableName = "community_listings")
data class CommunityListing(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val price: String, // e.g. "$15" or "Rent"
    val contact: String,
    val section: String, // "BuySell", "LostFound", "Notices"
    val dateAdded: String
)

// ==========================================
// MODULE 9 — SERVICE MARKETPLACE ENTITIES
// ==========================================

@Entity(tableName = "service_bookings")
data class ServiceBooking(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val providerName: String,
    val serviceCategory: String, // "Tutors", "Developers", "Designers", etc.
    val date: String,
    val cost: String,
    val status: String, // "Pending", "Confirmed", "Completed"
    val rating: Int = 5
)

// ==========================================
// DAOS FOR DATA ACCESS
// ==========================================

@Dao
interface LifeHubDao {

    // Notes
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%'")
    fun searchNotes(query: String): Flow<List<Note>>


    // Assignments
    @Query("SELECT * FROM assignments ORDER BY id DESC")
    fun getAllAssignments(): Flow<List<Assignment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: Assignment)

    @Delete
    suspend fun deleteAssignment(assignment: Assignment)


    // Attendance
    @Query("SELECT * FROM attendance")
    fun getAllAttendance(): Flow<List<Attendance>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: Attendance)

    @Delete
    suspend fun deleteAttendance(attendance: Attendance)


    // Timetables & Exams
    @Query("SELECT * FROM timetables ORDER BY id ASC")
    fun getAllTimetables(): Flow<List<Timetable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimetable(timetable: Timetable)

    @Delete
    suspend fun deleteTimetable(timetable: Timetable)

    @Query("SELECT * FROM exams ORDER BY date ASC")
    fun getAllExams(): Flow<List<Exam>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExam(exam: Exam)

    @Delete
    suspend fun deleteExam(exam: Exam)


    // Internships
    @Query("SELECT * FROM internships ORDER BY id DESC")
    fun getAllInternships(): Flow<List<Internship>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInternship(internship: Internship)


    // Study Materials
    @Query("SELECT * FROM study_materials ORDER BY id DESC")
    fun getAllStudyMaterials(): Flow<List<StudyMaterial>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyMaterial(material: StudyMaterial)


    // Documents & Digital Vault
    @Query("SELECT * FROM documents ORDER BY dateAdded DESC")
    fun getAllDocuments(): Flow<List<Document>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: Document)

    @Delete
    suspend fun deleteDocument(document: Document)


    // Identities
    @Query("SELECT * FROM identities ORDER BY id DESC")
    fun getAllIdentities(): Flow<List<IdentityCard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIdentity(identity: IdentityCard)

    @Delete
    suspend fun deleteIdentity(identity: IdentityCard)


    // Career Profiles
    @Query("SELECT * FROM career_profiles WHERE id = 1")
    fun getCareerProfile(): Flow<CareerProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCareerProfile(profile: CareerProfile)


    // Finance
    @Query("SELECT * FROM finance_items ORDER BY date DESC")
    fun getAllFinanceItems(): Flow<List<FinanceItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinanceItem(item: FinanceItem)

    @Delete
    suspend fun deleteFinanceItem(item: FinanceItem)


    // Assets
    @Query("SELECT * FROM assets ORDER BY id DESC")
    fun getAllAssets(): Flow<List<Asset>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsset(asset: Asset)

    @Delete
    suspend fun deleteAsset(asset: Asset)


    // Family
    @Query("SELECT * FROM family_members ORDER BY relationship ASC")
    fun getAllFamilyMembers(): Flow<List<FamilyMember>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFamilyMember(member: FamilyMember)

    @Delete
    suspend fun deleteFamilyMember(member: FamilyMember)


    // Community
    @Query("SELECT * FROM community_listings ORDER BY id DESC")
    fun getAllCommunityListings(): Flow<List<CommunityListing>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunityListing(listing: CommunityListing)

    @Delete
    suspend fun deleteCommunityListing(listing: CommunityListing)


    // Services
    @Query("SELECT * FROM service_bookings ORDER BY id DESC")
    fun getAllServiceBookings(): Flow<List<ServiceBooking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServiceBooking(booking: ServiceBooking)

    @Delete
    suspend fun deleteServiceBooking(booking: ServiceBooking)
}

// ==========================================
// CENTRAL DATABASE INSTANCE
// ==========================================

@Database(
    entities = [
        Note::class,
        Assignment::class,
        Attendance::class,
        Timetable::class,
        Exam::class,
        Internship::class,
        StudyMaterial::class,
        Document::class,
        IdentityCard::class,
        CareerProfile::class,
        FinanceItem::class,
        Asset::class,
        FamilyMember::class,
        CommunityListing::class,
        ServiceBooking::class
    ],
    version = 2,
    exportSchema = false
)
abstract class LifeHubDatabase : RoomDatabase() {
    abstract fun dao(): LifeHubDao
}

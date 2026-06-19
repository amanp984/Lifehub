package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import com.example.ui.theme.*

@Composable
fun LifeHubMainScreen(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()
    val aiResult by viewModel.aiResult.collectAsStateWithLifecycle()
    val isAiLoading by viewModel.isAiLoading.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { LifeHubTopAppBar(currentScreen, isPremium, viewModel) },
        bottomBar = { LifeHubBottomNavigation(currentScreen) { viewModel.currentScreen.value = it } },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                },
                label = "ScreenTransition"
            ) { screen ->
                when (screen) {
                    Screen.Dashboard -> DashboardScreen(viewModel)
                    Screen.StudentHub -> StudentHubScreen(viewModel, aiResult, isAiLoading)
                    Screen.DigitalVault -> DigitalVaultScreen(viewModel)
                    Screen.IdentityVault -> IdentityVaultScreen(viewModel)
                    Screen.CareerHub -> CareerHubScreen(viewModel, aiResult, isAiLoading)
                    Screen.FinanceHub -> FinanceHubScreen(viewModel)
                    Screen.AssetManager -> AssetScreen(viewModel)
                    Screen.FamilyHub -> FamilyScreen(viewModel)
                    Screen.CommunityHub -> CommunityScreen(viewModel)
                    Screen.ServiceMarketplace -> ServiceScreen(viewModel)
                    Screen.AdminPanel -> AdminPanelScreen(viewModel)
                    Screen.Profile -> ProfileScreen(viewModel)
                }
            }
        }
    }
}

// ==========================================
// SUB-COMPONENTS: HEADER & NAVIGATION
// ==========================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifeHubTopAppBar(currentScreen: Screen, isPremium: Boolean, viewModel: MainViewModel) {
    CenterAlignedTopAppBar(
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "LifeHub",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = (-0.5).sp
                    )
                    if (isPremium) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(AccentBlue)
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "PRO",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 8.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
                Text(
                    text = "LIFE OPERATING SYSTEM",
                    fontWeight = FontWeight.Bold,
                    fontSize = 8.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                    letterSpacing = 1.5.sp
                )
            }
        },
        navigationIcon = {
            if (currentScreen != Screen.Dashboard) {
                IconButton(onClick = { viewModel.currentScreen.value = Screen.Dashboard }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { viewModel.currentScreen.value = Screen.Profile }) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun LifeHubBottomNavigation(currentScreen: Screen, onNavigate: (Screen) -> Unit) {
    Column {
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), thickness = 1.dp)
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
        ) {
        NavigationBarItem(
            selected = currentScreen == Screen.Dashboard,
            onClick = { onNavigate(Screen.Dashboard) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.testTag("nav_home")
        )
        NavigationBarItem(
            selected = currentScreen == Screen.StudentHub,
            onClick = { onNavigate(Screen.StudentHub) },
            icon = { Icon(Icons.Default.List, contentDescription = "Student") },
            label = { Text("Student", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.testTag("nav_student")
        )
        NavigationBarItem(
            selected = currentScreen == Screen.DigitalVault,
            onClick = { onNavigate(Screen.DigitalVault) },
            icon = { Icon(Icons.Default.Lock, contentDescription = "Vault") },
            label = { Text("Vault", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.testTag("nav_vault")
        )
        NavigationBarItem(
            selected = currentScreen == Screen.AdminPanel,
            onClick = { onNavigate(Screen.AdminPanel) },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Admin") },
            label = { Text("Admin", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.testTag("nav_admin")
        )
    }
}
}

// ==========================================
// 1. DASHBOARD SCREEN
// ==========================================

@Composable
fun DashboardScreen(viewModel: MainViewModel) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    val docs by viewModel.documents.collectAsStateWithLifecycle()
    val finance by viewModel.financeItems.collectAsStateWithLifecycle()
    val family by viewModel.familyMembers.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero banner matching "Community Notice" aesthetic
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(26.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                        )
                    )
                    .border(1.dp, Color(0xFF334155).copy(alpha = 0.3f), RoundedCornerShape(26.dp))
                    .padding(20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(BluePrimary)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "PREMIUM",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                letterSpacing = 1.sp
                            )
                        }
                        Text(
                            text = "LifeHub Active",
                            color = Color(0xFF38BDF8),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Text(
                        text = "Your Life Operating System",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = Color.White,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "10 comprehensive operational hubs unified into one secure experience.",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
        }

        // Search Bar matching mockup
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                    .clickable { /* Fast Filter action placeholder */ }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search input placeholder",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Search your life...",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.61f)
                    )
                }
            }
        }

        // Feature Grid Options
        item {
            Text(
                text = "Operational Hubs",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = 0.5.sp
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    HubSelectionCard(
                        title = "Student Hub",
                        desc = "${notes.size} Active syllabus items",
                        icon = Icons.Default.List,
                        accent = AccentBlue,
                        modifier = Modifier.weight(1f)
                    ) { viewModel.currentScreen.value = Screen.StudentHub }

                    HubSelectionCard(
                        title = "Digital Vault",
                        desc = "${docs.size} Encrypted files",
                        icon = Icons.Default.Lock,
                        accent = AccentMint,
                        modifier = Modifier.weight(1f)
                    ) { viewModel.currentScreen.value = Screen.DigitalVault }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    HubSelectionCard(
                        title = "Identity Vault",
                        desc = "E2E secure storage",
                        icon = Icons.Default.CheckCircle,
                        accent = AccentCoral,
                        modifier = Modifier.weight(1f)
                    ) { viewModel.currentScreen.value = Screen.IdentityVault }

                    HubSelectionCard(
                        title = "Career Hub",
                        desc = "AI Optimizer & Profile",
                        icon = Icons.Default.Build,
                        accent = BluePrimary,
                        modifier = Modifier.weight(1f)
                    ) { viewModel.currentScreen.value = Screen.CareerHub }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    HubSelectionCard(
                        title = "Finance Hub",
                        desc = "Budget: \$${finance.filter { it.type == "Income" }.sumOf { it.amount } - finance.filter { it.type == "Expense" }.sumOf { it.amount }}",
                        icon = Icons.Default.ShoppingCart,
                        accent = AccentMint,
                        modifier = Modifier.weight(1f)
                    ) { viewModel.currentScreen.value = Screen.FinanceHub }

                    HubSelectionCard(
                        title = "Asset Manager",
                        desc = "Warranties & Gadgets",
                        icon = Icons.Default.Check,
                        accent = AccentBlue,
                        modifier = Modifier.weight(1f)
                    ) { viewModel.currentScreen.value = Screen.AssetManager }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    HubSelectionCard(
                        title = "Family Hub",
                        desc = "${family.size} Emergency contact list",
                        icon = Icons.Default.Person,
                        accent = AccentCoral,
                        modifier = Modifier.weight(1f)
                    ) { viewModel.currentScreen.value = Screen.FamilyHub }

                    HubSelectionCard(
                        title = "Community",
                        desc = "Buy, Sell & Lost-Found",
                        icon = Icons.Default.Share,
                        accent = AccentBlue,
                        modifier = Modifier.weight(1f)
                    ) { viewModel.currentScreen.value = Screen.CommunityHub }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    HubSelectionCard(
                        title = "Services",
                        desc = "Book freelancers & tutors",
                        icon = Icons.Default.Star,
                        accent = AccentMint,
                        modifier = Modifier.weight(1f)
                    ) { viewModel.currentScreen.value = Screen.ServiceMarketplace }

                    HubSelectionCard(
                        title = "Profile & Plan",
                        desc = "Upgrades & Analytics",
                        icon = Icons.Default.Settings,
                        accent = AccentBlue,
                        modifier = Modifier.weight(1f)
                    ) { viewModel.currentScreen.value = Screen.Profile }
                }
            }
        }
    }
}

@Composable
fun HubSelectionCard(
    title: String,
    desc: String,
    icon: ImageVector,
    accent: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(115.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(accent.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = accent, modifier = Modifier.size(18.dp))
                }
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
            }

            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                Text(text = desc, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

// ==========================================
// 2. STUDENT HUB
// ==========================================

@Composable
fun StudentHubScreen(viewModel: MainViewModel, aiResult: String?, isAiLoading: Boolean) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    val assignments by viewModel.assignments.collectAsStateWithLifecycle()
    val attendance by viewModel.attendanceList.collectAsStateWithLifecycle()

    var noteTitle by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }
    var noteCategory by remember { mutableStateOf("Academics") }

    var assTitle by remember { mutableStateOf("") }
    var assSubject by remember { mutableStateOf("") }
    var assDue by remember { mutableStateOf("2026-06-30") }
    var assPriority by remember { mutableStateOf("High") }

    var searchNoteQuery by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Module 1: Student Operating Hub",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Attendance Monitor
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "Attendance Monitor", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    if (attendance.isEmpty()) {
                        Text(text = "No subjects tracked. Use Admin to seed default syllabus items.", fontSize = 12.sp, color = OnSurfaceMutedDark)
                    } else {
                        attendance.forEach { item ->
                            val pct = if (item.total > 0) (item.attended.toFloat() / item.total.toFloat() * 100).toInt() else 0
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = item.subject, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                    Text(text = "${item.attended}/${item.total} lectures • $pct%", fontSize = 11.sp, color = if (pct >= 75) AccentMint else AccentCoral)
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Button(
                                        onClick = { viewModel.updateAttendanceCount(item, true) },
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                        modifier = Modifier.height(30.dp)
                                    ) {
                                        Text("+ Present", fontSize = 10.sp)
                                    }
                                    Button(
                                        onClick = { viewModel.updateAttendanceCount(item, false) },
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = AccentCoral),
                                        modifier = Modifier.height(30.dp)
                                    ) {
                                        Text("+ Absent", fontSize = 10.sp, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Notes and AI Summarization
        item {
            Text(text = "Personal Lecture Notes", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Add Classroom Note", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    OutlinedTextField(
                        value = noteTitle,
                        onValueChange = { noteTitle = it },
                        label = { Text("Note Title") },
                        modifier = Modifier.fillMaxWidth().testTag("student_note_title")
                    )
                    OutlinedTextField(
                        value = noteContent,
                        onValueChange = { noteContent = it },
                        label = { Text("Describe details...") },
                        modifier = Modifier.fillMaxWidth().height(100.dp).testTag("student_note_content")
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf("Academics", "Career", "Personal").forEach { cat ->
                                FilterChip(
                                    selected = noteCategory == cat,
                                    onClick = { noteCategory = cat },
                                    label = { Text(cat, fontSize = 11.sp) }
                                )
                            }
                        }
                        Button(
                            onClick = {
                                if (noteTitle.isNotEmpty() && noteContent.isNotEmpty()) {
                                    viewModel.addNote(noteTitle, noteContent, noteCategory)
                                    noteTitle = ""
                                    noteContent = ""
                                }
                            },
                            modifier = Modifier.testTag("student_add_note_btn")
                        ) {
                            Text("Save Note")
                        }
                    }
                }
            }
        }

        // Active Notes list & AI summaries
        item {
            OutlinedTextField(
                value = searchNoteQuery,
                onValueChange = { searchNoteQuery = it },
                label = { Text("Search lecture notes...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // AI Workspace Result dialog inside Screen
        if (isAiLoading || aiResult != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "AI Workstation Live Outline", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            IconButton(onClick = { viewModel.clearAiOutput() }) {
                                Icon(Icons.Default.Delete, contentDescription = "Close", tint = AccentCoral)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (isAiLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            Text(text = "Asking Gemini for premium notes summarization...", fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                        } else {
                            Text(text = aiResult ?: "", fontSize = 13.sp, lineHeight = 19.sp)
                        }
                    }
                }
            }
        }

        val filteredNotes = notes.filter {
            it.title.contains(searchNoteQuery, true) || it.content.contains(searchNoteQuery, true)
        }

        if (filteredNotes.isEmpty()) {
            item {
                Text(
                    text = "No notes matched your current search.",
                    fontSize = 12.sp,
                    color = OnSurfaceMutedDark,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(14.dp)
                )
            }
        } else {
            items(filteredNotes) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(AccentBlue.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(text = item.category, fontSize = 9.sp, color = AccentBlue, fontWeight = FontWeight.Bold)
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                TextButton(
                                    onClick = { viewModel.summarizeNote(item.title, item.content) },
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("✨ AI Summary", fontSize = 11.sp, color = AccentBlue)
                                }
                                IconButton(onClick = { viewModel.deleteNote(item) }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = AccentCoral, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = item.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = item.content, fontSize = 12.sp, color = OnSurfaceMutedDark, lineHeight = 17.sp)
                    }
                }
            }
        }

        // Assignment Trackers
        item {
            Text(text = "Assignments Tracker", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Insert New Assignment Task", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = assTitle,
                            onValueChange = { assTitle = it },
                            label = { Text("Title") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = assSubject,
                            onValueChange = { assSubject = it },
                            label = { Text("Subject") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = assDue,
                            onValueChange = { assDue = it },
                            label = { Text("Due Date") },
                            modifier = Modifier.weight(1f)
                        )
                        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf("High", "Medium").forEach { p ->
                                FilterChip(
                                    selected = assPriority == p,
                                    onClick = { assPriority = p },
                                    label = { Text(p, fontSize = 10.sp) }
                                )
                            }
                        }
                    }
                    Button(
                        onClick = {
                            if (assTitle.isNotEmpty() && assSubject.isNotEmpty()) {
                                viewModel.addAssignment(assTitle, assSubject, assDue, assPriority)
                                assTitle = ""
                                assSubject = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Add Task")
                    }
                }
            }
        }

        items(assignments) { item ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(11.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = item.status == "Completed",
                            onCheckedChange = { viewModel.updateAssignmentStatus(item, it) }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = item.title,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = if (item.status == "Completed") OnSurfaceMutedDark else MaterialTheme.colorScheme.onSurface
                            )
                            Text(text = "${item.subject} • Due: ${item.dueDate}", fontSize = 11.sp, color = OnSurfaceMutedDark)
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (item.priority == "High") AccentCoral.copy(alpha = 0.15f) else AccentBlue.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = item.priority, fontSize = 9.sp, color = if (item.priority == "High") AccentCoral else AccentBlue, fontWeight = FontWeight.Bold)
                        }
                        IconButton(onClick = { viewModel.deleteAssignment(item) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = AccentCoral)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. DIGITAL VAULT
// ==========================================

@Composable
fun DigitalVaultScreen(viewModel: MainViewModel) {
    val documents by viewModel.documents.collectAsStateWithLifecycle()

    var docName by remember { mutableStateOf("") }
    var docCategory by remember { mutableStateOf("Certificates") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Module 2: Digital Encrypted Vault",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Securely catalog academic degree files, identity PDFs, resumes and personal proof backups with active download logs", fontSize = 12.sp, color = OnSurfaceMutedDark)
        }

        // Add Document Form
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Upload Virtual Document", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    OutlinedTextField(
                        value = docName,
                        onValueChange = { docName = it },
                        label = { Text("Document File Name (e.g. Master_Thesis_Draft.pdf)") },
                        modifier = Modifier.fillMaxWidth().testTag("doc_name_input")
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Certificates", "Resumes", "Portfolios").forEach { cat ->
                            FilterChip(
                                selected = docCategory == cat,
                                onClick = { docCategory = cat },
                                label = { Text(cat, fontSize = 11.sp) }
                            )
                        }
                    }
                    Button(
                        onClick = {
                            if (docName.isNotEmpty()) {
                                viewModel.addDocument(docName, "1.4 MB", docCategory)
                                docName = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("doc_save_btn")
                    ) {
                        Text("Simulate Secure Upload")
                    }
                }
            }
        }

        // Active Documents list (Grid / List)
        item {
            Text(text = "Active Cloud Backups (2 GB Allowed on Free)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        if (documents.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Your secure digital archive is currently empty.", fontSize = 12.sp, color = OnSurfaceMutedDark)
                }
            }
        } else {
            items(documents) { doc ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(AccentMint.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = AccentMint, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = doc.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(text = "Category: ${doc.category} • Size: ${doc.size}", fontSize = 11.sp, color = OnSurfaceMutedDark)
                            }
                        }
                        IconButton(onClick = { viewModel.deleteDocument(doc) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = AccentCoral)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 4. IDENTITY VAULT
// ==========================================

@Composable
fun IdentityVaultScreen(viewModel: MainViewModel) {
    val identities by viewModel.identities.collectAsStateWithLifecycle()

    var cardType by remember { mutableStateOf("Aadhaar Card") }
    var holderName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("N/A") }
    var secureNote by remember { mutableStateOf("") }

    var passcodeWord by remember { mutableStateOf("") }
    var accessGranted by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Module 3: Secure Identity Vault",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "End-to-End Encrypted database holding official document copies", fontSize = 12.sp, color = OnSurfaceMutedDark)
        }

        // Passcode Protection Simulation
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (accessGranted) MaterialTheme.colorScheme.surface else DarkCardSurface
                ),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = if (accessGranted) "✓ Identity Decryption Key Active" else "🔒 Cryptographic Decryption Lock",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (accessGranted) AccentMint else AccentCoral
                    )
                    if (!accessGranted) {
                        Text(text = "To preview masked/secured records (Aadhaar, PAN, SSN), enter your local LifeHub device verification key.", fontSize = 12.sp, color = OnSurfaceMutedDark)
                        OutlinedTextField(
                            value = passcodeWord,
                            onValueChange = { passcodeWord = it },
                            label = { Text("Verification PIN") },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            modifier = Modifier.fillMaxWidth().testTag("passcode_field")
                        )
                        Button(
                            onClick = {
                                if (passcodeWord == "1234") {
                                    accessGranted = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth().testTag("unlock_btn")
                        ) {
                            Text("Simulate Biometric Verification")
                        }
                        Text(text = "*Prototype Tip: Enter pin '1234' to unlock sensitive fields.", fontSize = 10.sp, color = AccentBlue)
                    } else {
                        Button(
                            onClick = {
                                accessGranted = false
                                passcodeWord = ""
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.outline)
                        ) {
                            Text("Relock Vault")
                        }
                    }
                }
            }
        }

        if (accessGranted) {
            // Document creation form
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(text = "Insert Identity Card Details", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Aadhaar", "PAN", "Passport").forEach { card ->
                                FilterChip(
                                    selected = cardType.startsWith(card),
                                    onClick = { cardType = "$card Card" },
                                    label = { Text(card, fontSize = 10.sp) }
                                )
                            }
                        }
                        OutlinedTextField(
                            value = holderName,
                            onValueChange = { holderName = it },
                            label = { Text("Holder Full Name") },
                            modifier = Modifier.fillMaxWidth().testTag("identity_holder")
                        )
                        OutlinedTextField(
                            value = cardNumber,
                            onValueChange = { cardNumber = it },
                            label = { Text("Official ID Number / Unique Hash") },
                            modifier = Modifier.fillMaxWidth().testTag("identity_number")
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = expiryDate,
                                onValueChange = { expiryDate = it },
                                label = { Text("Expiry (if any)") },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = secureNote,
                                onValueChange = { secureNote = it },
                                label = { Text("Secure Recovery Notes") },
                                modifier = Modifier.weight(1.5f)
                            )
                        }
                        Button(
                            onClick = {
                                if (holderName.isNotEmpty() && cardNumber.isNotEmpty()) {
                                    viewModel.addIdentity(cardType, holderName, cardNumber, expiryDate, secureNote)
                                    holderName = ""
                                    cardNumber = ""
                                }
                            },
                            modifier = Modifier.align(Alignment.End).testTag("save_identity_btn")
                        ) {
                            Text("Enforce Secure Save")
                        }
                    }
                }
            }

            // Cards display list
            items(identities) { id ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = id.cardType, fontWeight = FontWeight.Bold, color = AccentBlue, fontSize = 14.sp)
                            IconButton(onClick = { viewModel.deleteIdentity(id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = AccentCoral)
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline)
                        Text(text = "Holder: ${id.holderName}", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Text(text = "ID Number: ${id.cardNumber}", fontSize = 13.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold)
                        Text(text = "Notes: ${id.secureNote}", fontSize = 11.sp, color = OnSurfaceMutedDark)
                    }
                }
            }
        }
    }
}

// ==========================================
// 5. CAREER HUB
// ==========================================

@Composable
fun CareerHubScreen(viewModel: MainViewModel, aiResult: String?, isAiLoading: Boolean) {
    val profile by viewModel.careerProfile.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }
    var achievements by remember { mutableStateOf("") }
    var education by remember { mutableStateOf("") }

    var isEditing by remember { mutableStateOf(false) }

    LaunchedEffect(profile) {
        profile?.let {
            name = it.fullName
            email = it.email
            phone = it.phone
            bio = it.bio
            skills = it.skills
            achievements = it.achievements
            education = it.education
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Module 4: Career Optimization Workspace",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Build professional CV matrices, catalog skills, achievements, and use AI optimizing summary highlights", fontSize = 12.sp, color = OnSurfaceMutedDark)
        }

        // Active CV Panel
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "My Digital Resume Base", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Button(onClick = { isEditing = !isEditing }) {
                            Text(if (isEditing) "Close" else "Update Profile")
                        }
                    }

                    if (isEditing) {
                        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email Contact") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = bio, onValueChange = { bio = it }, label = { Text("Profile Executive Summary") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = skills, onValueChange = { skills = it }, label = { Text("Core Stack / Skills (comma separated)") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = achievements, onValueChange = { achievements = it }, label = { Text("Achievements (one per line)") }, modifier = Modifier.fillMaxWidth().height(100.dp))
                        OutlinedTextField(value = education, onValueChange = { education = it }, label = { Text("Education Highlights") }, modifier = Modifier.fillMaxWidth())

                        Button(
                            onClick = {
                                viewModel.saveCareerProfile(name, email, phone, bio, skills, achievements, education)
                                isEditing = false
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Enforce Cache Save")
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(text = name.ifEmpty { "Profile Name Placeholder" }, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                            Text(text = "$email • $phone", fontSize = 12.sp, color = OnSurfaceMutedDark)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Executive Summary:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text(text = bio.ifEmpty { "No executive summary updated yet." }, fontSize = 12.sp, lineHeight = 17.sp)

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Skills & Expertise Stack:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                if (skills.isEmpty()) {
                                    Text("No skills updated", fontSize = 11.sp)
                                } else {
                                    skills.split(",").forEach { s ->
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(AccentBlue.copy(alpha = 0.15f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(text = s.trim(), fontSize = 10.sp, color = AccentBlue)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Achievements:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text(text = achievements.ifEmpty { "No achievements written." }, fontSize = 12.sp, lineHeight = 17.sp)

                            Spacer(modifier = Modifier.height(6.dp))
                            Button(
                                onClick = { viewModel.buildAiResumeOptimize(name, bio, skills, achievements) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("✨ AI Executive Resume Enhancer")
                            }
                        }
                    }
                }
            }
        }

        // Display AI response
        if (isAiLoading || aiResult != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "✨ AI Career Counselor Results", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            IconButton(onClick = { viewModel.clearAiOutput() }) {
                                Icon(Icons.Default.Delete, contentDescription = "Close", tint = AccentCoral)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (isAiLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            Text(text = "Optimizing bio parameters with Gemini...", fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                        } else {
                            Text(text = aiResult ?: "", fontSize = 13.sp, lineHeight = 19.sp)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 6. FINANCE HUB
// ==========================================

@Composable
fun FinanceHubScreen(viewModel: MainViewModel) {
    val items by viewModel.financeItems.collectAsStateWithLifecycle()

    var amount by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Food") }
    var itemType by remember { mutableStateOf("Expense") } // Expense, Income, Subscription, Bill, EMI

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Module 5: Finance Hub & Reminders",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Summary Statistics
        item {
            val totalIncome = items.filter { it.type == "Income" }.sumOf { it.amount }
            val totalExpenses = items.filter { it.type != "Income" }.sumOf { it.amount }
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Total Inflow", fontSize = 11.sp, color = OnSurfaceMutedDark)
                        Text(text = "\$${"%.2f".format(totalIncome)}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = AccentMint)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Total Expenses", fontSize = 11.sp, color = OnSurfaceMutedDark)
                        Text(text = "\$${"%.2f".format(totalExpenses)}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = AccentCoral)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Operational Net", fontSize = 11.sp, color = OnSurfaceMutedDark)
                        Text(text = "\$${"%.2f".format(totalIncome - totalExpenses)}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = AccentBlue)
                    }
                }
            }
        }

        // Expense Form
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Add Transaction", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf("Expense", "Income", "Subscription", "Bill").forEach { t ->
                            FilterChip(
                                selected = itemType == t,
                                onClick = { itemType = t },
                                label = { Text(t, fontSize = 10.sp) }
                            )
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Amount ($)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f).testTag("finance_amount")
                        )
                        OutlinedTextField(
                            value = desc,
                            onValueChange = { desc = it },
                            label = { Text("Description") },
                            modifier = Modifier.weight(1.5f).testTag("finance_desc")
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf("Food", "Rent", "Utility", "Transport").forEach { cat ->
                                FilterChip(
                                    selected = category == cat,
                                    onClick = { category = cat },
                                    label = { Text(cat, fontSize = 10.sp) }
                                )
                            }
                        }
                        Button(
                            onClick = {
                                val amt = amount.toDoubleOrNull() ?: 0.0
                                if (amt > 0.0 && desc.isNotEmpty()) {
                                    viewModel.addFinanceItem(amt, category, "2026-06-19", desc, itemType, "28th Monthly")
                                    amount = ""
                                    desc = ""
                                }
                            },
                            modifier = Modifier.testTag("finance_add_btn")
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }

        item {
            Text(text = "Transaction History", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        items(items) { item ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (item.type == "Income") AccentMint.copy(alpha = 0.15f) else AccentCoral.copy(alpha = 0.15f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (item.type == "Income") Icons.Default.Add else Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = if (item.type == "Income") AccentMint else AccentCoral,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = item.description, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = "Category: ${item.category} • ${item.type}", fontSize = 11.sp, color = OnSurfaceMutedDark)
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "${if (item.type == "Income") "+" else "-"}\$${item.amount}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (item.type == "Income") AccentMint else AccentCoral
                        )
                        IconButton(onClick = { viewModel.deleteFinanceItem(item) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = AccentCoral)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 7. ASSET MANAGER
// ==========================================

@Composable
fun AssetScreen(viewModel: MainViewModel) {
    val assets by viewModel.assets.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Electronics") }
    var value by remember { mutableStateOf("") }
    var serialNumber by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Module 6: Enterprise Asset Hub",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Keep track of physical machinery, personal electronics, warranty schedules and vehicle files", fontSize = 12.sp, color = OnSurfaceMutedDark)
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Record Physical Asset", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Electronics", "Vehicles", "Appliances").forEach { cat ->
                            FilterChip(
                                selected = category == cat,
                                onClick = { category = cat },
                                label = { Text(cat, fontSize = 10.sp) }
                            )
                        }
                    }
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Asset / Gadget Name") },
                        modifier = Modifier.fillMaxWidth().testTag("asset_name")
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = value,
                            onValueChange = { value = it },
                            label = { Text("Purchase Value ($)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f).testTag("asset_value")
                        )
                        OutlinedTextField(
                            value = serialNumber,
                            onValueChange = { serialNumber = it },
                            label = { Text("Serial Number") },
                            modifier = Modifier.weight(1.2f).testTag("asset_serial")
                        )
                    }
                    Button(
                        onClick = {
                            val valD = value.toDoubleOrNull() ?: 0.0
                            if (name.isNotEmpty() && valD > 0.0) {
                                viewModel.addAsset(name, category, valD, "2026-06-19", "2027-06-19", serialNumber)
                                name = ""
                                value = ""
                                serialNumber = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("asset_save_btn")
                    ) {
                        Text("Save Asset")
                    }
                }
            }
        }

        item {
            Text(text = "Currently Logged Assets", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        items(assets) { asset ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = asset.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "Category: ${asset.category} • SN: ${asset.serialNumber.ifEmpty { "N/A" }}", fontSize = 11.sp, color = OnSurfaceMutedDark)
                        Text(text = "Warranty Expiry: ${asset.warrantyExpiry}", fontSize = 11.sp, color = AccentBlue)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "\$${asset.value}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AccentMint)
                        IconButton(onClick = { viewModel.deleteAsset(asset) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = AccentCoral)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 8. FAMILY HUB
// ==========================================

@Composable
fun FamilyScreen(viewModel: MainViewModel) {
    val family by viewModel.familyMembers.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf("") }
    var relationship by remember { mutableStateOf("Father") }
    var contact by remember { mutableStateOf("") }
    var medicines by remember { mutableStateOf("") }
    var appointments by remember { mutableStateOf("") }
    var emergencyContact by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Module 7: Secure Family & Medical Hub",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Manage emergency family speed-dials, care records, appointments and medicine notifications", fontSize = 12.sp, color = OnSurfaceMutedDark)
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Enroll Family Care Block", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf("Father", "Mother", "Sibling", "Spouse").forEach { r ->
                            FilterChip(
                                selected = relationship == r,
                                onClick = { relationship = r },
                                label = { Text(r, fontSize = 10.sp) }
                            )
                        }
                    }
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth().testTag("fam_name")
                    )
                    OutlinedTextField(
                        value = contact,
                        onValueChange = { contact = it },
                        label = { Text("Emergency Contact/Phone") },
                        modifier = Modifier.fillMaxWidth().testTag("fam_phone")
                    )
                    OutlinedTextField(
                        value = medicines,
                        onValueChange = { medicines = it },
                        label = { Text("Active Medical Prescriptions / Reminders") },
                        modifier = Modifier.fillMaxWidth().testTag("fam_meds")
                    )
                    OutlinedTextField(
                        value = appointments,
                        onValueChange = { appointments = it },
                        label = { Text("Upcoming Scheduled Doctor Appointments") },
                        modifier = Modifier.fillMaxWidth().testTag("fam_appt")
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = emergencyContact, onCheckedChange = { emergencyContact = it })
                        Text(text = "Designate as Primary Emergency Contact", fontSize = 12.sp)
                    }
                    Button(
                        onClick = {
                            if (name.isNotEmpty() && contact.isNotEmpty()) {
                                viewModel.addFamily(name, relationship, contact, medicines, appointments, emergencyContact)
                                name = ""
                                contact = ""
                                medicines = ""
                                appointments = ""
                                emergencyContact = false
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("fam_save_btn")
                    ) {
                        Text("Save Details")
                    }
                }
            }
        }

        item {
            Text(text = "Family Records Dossier", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        items(family) { member ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(14.dp).fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = member.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(AccentBlue.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(text = member.relationship, fontSize = 9.sp, color = AccentBlue)
                            }
                            if (member.emergencyContact) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(AccentCoral.copy(alpha = 0.15f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(text = "EMERGENCY SPEED-DIAL", fontSize = 8.sp, color = AccentCoral, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        IconButton(onClick = { viewModel.deleteFamily(member) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = AccentCoral)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Phone: ${member.contact}", fontSize = 12.sp, color = OnSurfaceMutedDark)
                    if (member.medicines.isNotEmpty()) {
                        Text(text = "Medicines: ${member.medicines}", fontSize = 12.sp, color = AccentCoral)
                    }
                    if (member.appointments.isNotEmpty()) {
                        Text(text = "Next Appointment: ${member.appointments}", fontSize = 12.sp, color = AccentMint)
                    }
                }
            }
        }
    }
}

// ==========================================
// 9. COMMUNITY HUB
// ==========================================

@Composable
fun CommunityScreen(viewModel: MainViewModel) {
    val listings by viewModel.communityListings.collectAsStateWithLifecycle()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var section by remember { mutableStateOf("BuySell") } // BuySell, LostFound, Notices

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Module 8: Peer-to-Peer Hub",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Lost & found desk, local notice board and marketplace for books / scientific calculators", fontSize = 12.sp, color = OnSurfaceMutedDark)
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Post Announcement Desk", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf("BuySell", "LostFound", "Notices").forEach { s ->
                            FilterChip(
                                selected = section == s,
                                onClick = { section = s },
                                label = { Text(s, fontSize = 10.sp) }
                            )
                        }
                    }
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Describe details...") }, modifier = Modifier.fillMaxWidth())
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = price,
                            onValueChange = { price = it },
                            label = { Text("Price / Reward") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = contact,
                            onValueChange = { contact = it },
                            label = { Text("Contact Phone") },
                            modifier = Modifier.weight(1.2f)
                        )
                    }
                    Button(
                        onClick = {
                            if (title.isNotEmpty() && description.isNotEmpty()) {
                                viewModel.addCommunityListing(title, description, price, contact, section)
                                title = ""
                                description = ""
                                price = ""
                                contact = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Post Desk Listing")
                    }
                }
            }
        }

        item {
            Text(text = "Active Local Listings", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        items(listings) { list ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(12.dp).fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(AccentBlue.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = list.section, fontSize = 9.sp, color = AccentBlue, fontWeight = FontWeight.Bold)
                        }
                        IconButton(onClick = { viewModel.deleteCommunityListing(list) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = AccentCoral)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = list.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = list.description, fontSize = 12.sp, color = OnSurfaceMutedDark)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Price: ${list.price}", fontWeight = FontWeight.Bold, color = AccentMint, fontSize = 12.sp)
                        Text(text = "Tel: ${list.contact}", fontSize = 11.sp, color = OnSurfaceMutedDark)
                    }
                }
            }
        }
    }
}

// ==========================================
// 10. SERVICE MARKETPLACE
// ==========================================

@Composable
fun ServiceScreen(viewModel: MainViewModel) {
    val bookings by viewModel.serviceBookings.collectAsStateWithLifecycle()

    var provider by remember { mutableStateOf("Tutor Al Smith") }
    var category by remember { mutableStateOf("Tutors") }
    var cost by remember { mutableStateOf("$40/Hr") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Module 9: Freelance Service Hub",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Hire experienced course tutors, UI photographers, project developers and design specialists", fontSize = 12.sp, color = OnSurfaceMutedDark)
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Book Marketplace Specialist", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf("Tutors", "Developers", "Designers", "Photographers").forEach { c ->
                            FilterChip(
                                selected = category == c,
                                onClick = { category = c },
                                label = { Text(c, fontSize = 10.sp) }
                            )
                        }
                    }
                    OutlinedTextField(
                        value = provider,
                        onValueChange = { provider = it },
                        label = { Text("Provider Name / Agency") },
                        modifier = Modifier.fillMaxWidth().testTag("service_provider")
                    )
                    OutlinedTextField(
                        value = cost,
                        onValueChange = { cost = it },
                        label = { Text("Agreed Pricing Model / Terms") },
                        modifier = Modifier.fillMaxWidth().testTag("service_cost")
                    )
                    Button(
                        onClick = {
                            if (provider.isNotEmpty()) {
                                viewModel.addServiceBooking(provider, category, "2026-06-25", cost)
                                provider = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("service_book_btn")
                    ) {
                        Text("Simulate Slot Booking")
                    }
                }
            }
        }

        item {
            Text(text = "Active Contract Schedules", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        items(bookings) { book ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = book.providerName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "${book.serviceCategory} • Scheduled: ${book.date}", fontSize = 11.sp, color = OnSurfaceMutedDark)
                        Text(text = "Terms: ${book.cost}", fontSize = 12.sp, color = AccentBlue)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(AccentMint.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = book.status, fontSize = 9.sp, color = AccentMint, fontWeight = FontWeight.Bold)
                        }
                        IconButton(onClick = { viewModel.deleteServiceBooking(book) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Cancel", tint = AccentCoral)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 11. ADMIN PANEL SCREEN
// ==========================================

@Composable
fun AdminPanelScreen(viewModel: MainViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Module 10: Central Administration Workspace",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "System health, user analytics tracker, contract audits, and commission logs", fontSize = 12.sp, color = OnSurfaceMutedDark)
        }

        // System Health Metric Grid
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminMetricCard(title = "Database Sync", value = "Online (Room SQLite)", accent = AccentMint, modifier = Modifier.weight(1f))
                AdminMetricCard(title = "Security Token", value = "JWT-256 Valid", accent = AccentBlue, modifier = Modifier.weight(1f))
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminMetricCard(title = "Market Comm.", value = "15.0% Standard", accent = AccentBlue, modifier = Modifier.weight(1f))
                AdminMetricCard(title = "System Latency", value = "12 ms (Local)", accent = AccentMint, modifier = Modifier.weight(1f))
            }
        }

        // User Analytics & Performance Flow
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Security Audit Records", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    Text(text = "• Row Level Security: ACTIVE ON ALL SCHEMAS", fontSize = 12.sp, color = AccentMint)
                    Text(text = "• Cross-site Scripting Protect (XSS): ENFORCED", fontSize = 12.sp, color = AccentMint)
                    Text(text = "• Local Database Encrypt Layer: PIN AES-256 ENABLED", fontSize = 12.sp, color = AccentMint)
                    Text(text = "• Rate Limiting Filter: 15 Requests / Sec active", fontSize = 12.sp, color = OnSurfaceMutedDark)
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Database Recovery Commands", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = "If testing sandbox database operations, trigger model seeding directly to refresh components.", fontSize = 12.sp, color = OnSurfaceMutedDark)
                    Button(
                        onClick = {
                            // Seed default subjects and entries
                            viewModel.addAttendance("Compiler Theory", 12, 16)
                            viewModel.addAttendance("Advanced Databases", 14, 15)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth().testTag("admin_seed_btn")
                    ) {
                        Text("Inject New Syllabus Modules")
                    }
                }
            }
        }
    }
}

@Composable
fun AdminMetricCard(title: String, value: String, accent: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(85.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Text(text = title, fontSize = 11.sp, color = OnSurfaceMutedDark)
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = accent)
        }
    }
}

// ==========================================
// 12. PROFILE / SETTINGS SCREEN
// ==========================================

@Composable
fun ProfileScreen(viewModel: MainViewModel) {
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "My Profile & Subscription",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Premium showcase banner
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isPremium) MaterialTheme.colorScheme.primary else DarkCardSurface
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (isPremium) "✓ Premium Enterprise Membership Active" else "👑 Upgrade to Premium Plan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color.White
                    )
                    Text(
                        text = if (isPremium) "Your Digital Vault allocation is 100 GB. Core AI Note Summary and Elite Executive Resume highlights are fully unlocked." else "Unlock 100 GB Vault Storage and full unrestricted Access to Gemini AI Resume formatting and Classroom summary highlights.",
                        fontSize = 12.sp,
                        color = if (isPremium) Color.White.copy(alpha = 0.82f) else OnSurfaceMutedDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Button(
                        onClick = { viewModel.togglePremium() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isPremium) AccentMint else BluePrimary
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("premium_upgrade_btn")
                    ) {
                        Text(
                            text = if (isPremium) "Deactivate Plan" else "Select Premium Tier ($9.99/Mo)",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Device configurations / credentials tips
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(text = "Security Configuration Logs", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline)
                    Text(text = "User: Jane Doe", fontSize = 13.sp)
                    Text(text = "Device Hash ID: d2ba90f1eaef90b", fontSize = 11.sp, color = OnSurfaceMutedDark)
                    Text(text = "API Service Platform: Google Gemini AI", fontSize = 11.sp, color = OnSurfaceMutedDark)
                    Text(text = "Database: SQLite v3.40 (Room Engine local client)", fontSize = 11.sp, color = OnSurfaceMutedDark)
                }
            }
        }
    }
}

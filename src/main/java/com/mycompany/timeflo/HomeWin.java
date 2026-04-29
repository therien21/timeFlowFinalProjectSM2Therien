/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.timeflo;

import com.mycompany.timeflo.model.YouTubeVideo;
import com.mycompany.timeflo.service.YouTubeClient;
import com.mycompany.timeflo.service.ClaudeAIService;
import com.mycompany.timeflo.service.GoogleCalendarService;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import java.awt.Desktop;
import java.net.URI;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.SwingWorker;
import java.util.prefs.Preferences;
import com.mycompany.timeflo.manager.TaskManager;
import com.mycompany.timeflo.model.Task;
import com.mycompany.timeflo.manager.ScheduleManager;
import com.mycompany.timeflo.model.ScheduleItem;
import com.mycompany.timeflo.model.ScheduleTableModel;
import com.mycompany.timeflo.manager.RecipeManager;
import com.mycompany.timeflo.model.AppData;
import com.mycompany.timeflo.manager.AppSaveManager;
import com.mycompany.timeflo.model.Recipe;
import com.mycompany.timeflo.model.RecipeTableModel;


/**
 *
 * @author theri
 */
public class HomeWin extends javax.swing.JFrame {

    private ArrayList<ScheduleItem> scheduleItems;
    private ScheduleTableModel scheduleTableModel;
    private ArrayList<Recipe> recipeItems;
    private RecipeTableModel recipeTableModel;
    private DefaultListModel<String> dlmTasks;
    private TaskManager taskManager;
    private ScheduleManager scheduleManager;
    private RecipeManager recipeManager;
    private AppSaveManager saveManager;
    private final YouTubeClient youtubeClient = new YouTubeClient();
    private ClaudeAIService claudeService;
    private GoogleCalendarService calendarService;
    private static final String PREF_KEY_CLAUDE = "TIMEFLO_CLAUDE_API_KEY";
    private String apiKey;
    private static final String PREF_KEY_CALENDAR = "TIMEFLO_CALENDAR_API_KEY";
    private String calendarApiKey;
    private Preferences pref;
    private static final String PREF_KEY_FOR_APIKEY = "TIMEFLO_YOUTUBE_API_KEY";
    
    private void showSchedulePopup(){
        JComboBox<String> timeBox = new JComboBox<>();
        for (ScheduleItem item : scheduleItems){
            timeBox.addItem(item.getTime());
        }
        JTextField eventField = new JTextField(25);
        JComboBox<String> typeBox = new JComboBox<>(new String[] {
            "School", "Meal", "Workout", "Personal", "Work", "Open"});
        JTextField notesField = new JTextField(25);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Select Time:"));
        panel.add(timeBox);
        panel.add(new JLabel("Enter Event:"));
        panel.add(eventField);
        panel.add(new JLabel("Enter Type:"));
        panel.add(typeBox);
        panel.add(new JLabel("Enter Notes:"));
        panel.add(notesField);
        int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Add Schedule Item",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
            );
        if (result == JOptionPane.OK_OPTION){
            int index = timeBox.getSelectedIndex();
            String eventName = eventField.getText().trim();
            String type  = (String)typeBox.getSelectedItem();
            String notes = notesField.getText().trim();
            if ("Meal".equals(type)){
                if(recipeItems.size() == 0){
                    JOptionPane.showMessageDialog(this, "No recipes available yet.");
                    return;
                }
                JComboBox<String> recipeBox = new JComboBox<>();
                for (Recipe recipe : recipeItems){
                    recipeBox.addItem(recipe.getName());
                }
                int recipeResult = JOptionPane.showConfirmDialog(
                        this,
                        recipeBox,
                        "Select a Recipe",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );
                if (recipeResult == JOptionPane.OK_OPTION){
                    String selectedRecipe = (String) recipeBox.getSelectedItem();
                    Recipe chosenRecipe = null;
                    for (Recipe recipe : recipeItems){
                        if (recipe.getName().equals(selectedRecipe)){
                            chosenRecipe = recipe;
                            break;
                        }
                    }
                    if (chosenRecipe != null){
                        eventName = "Meal: " + chosenRecipe.getName();
                        if (notes.isEmpty()){
                            notes = "Prep: " + chosenRecipe.getPrepTime()
                                    + " | Ingredients: " + chosenRecipe.getIngredients()
                                    + " | Category: " + chosenRecipe.getCategory();
                        }    
                    }
                } else {
                    return;
                }
            }
            if (!eventName.isEmpty()){
                ScheduleItem item = scheduleItems.get(index);
                item.setEventName(eventName);
                item.setType(type);
                item.setNotes(notes);
                scheduleTableModel.fireTableRowsUpdated(index, index);
                
            } 
           
        }
    }
private void showJournalPopup() {
    javax.swing.JTextField moodField    = new javax.swing.JTextField(25);
    javax.swing.JTextField goalField    = new javax.swing.JTextField(25);
    javax.swing.JTextField mealField    = new javax.swing.JTextField(25);
    javax.swing.JTextField workoutField = new javax.swing.JTextField(25);

    javax.swing.JPanel panel = new javax.swing.JPanel();
    panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
    panel.add(new javax.swing.JLabel("Good morning! How are you feeling today?"));
    panel.add(moodField);
    panel.add(new javax.swing.JLabel("What's your #1 priority today?"));
    panel.add(goalField);
    panel.add(new javax.swing.JLabel("Are you cooking or eating out today?"));
    panel.add(mealField);
    panel.add(new javax.swing.JLabel("Any workout planned?"));
    panel.add(workoutField);

    int result = JOptionPane.showConfirmDialog(
        this, panel, "Daily Journal — TimeFlow",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
    );

    if (result == JOptionPane.OK_OPTION) {
        String journalText =
            "Mood: "     + moodField.getText().trim()    + "\n" +
            "Goal: "     + goalField.getText().trim()     + "\n" +
            "Meal plan: "+ mealField.getText().trim()     + "\n" +
            "Workout: "  + workoutField.getText().trim();

        StringBuilder scheduleText = new StringBuilder();
        for (ScheduleItem item : scheduleItems) {
            if (!"Empty".equals(item.getEventName())) {
                scheduleText.append(item.getTime())
                            .append(" - ").append(item.getEventName())
                            .append(" (").append(item.getType()).append(")\n");
            }
        }
        if (scheduleText.length() == 0) {
            scheduleText.append("No events scheduled yet.");
        }

        final String finalSchedule = scheduleText.toString();
        final String finalJournal  = journalText;

        if (!claudeService.hasApiKey()) {
            String key = JOptionPane.showInputDialog(this,
                "Paste your Claude API key to get AI recommendations:");
            if (key != null && !key.trim().isEmpty()) {
                claudeService.setApiKey(key.trim());
                pref.put(PREF_KEY_CLAUDE, key.trim());
            }
        }

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return claudeService.getDailyRecommendation(finalSchedule, finalJournal);
            }
            @Override
            protected void done() {
                try {
                    String recommendation = get();
                    JOptionPane.showMessageDialog(HomeWin.this,
                        "<html><body style='width:300px'><b>Your AI Daily Plan:</b><br><br>"
                        + recommendation.replace("\n", "<br>") + "</body></html>",
                        "TimeFlow AI Recommendation",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(HomeWin.this,
                        "AI recommendation unavailable right now.\n" +
                        "Tip: Stay focused on your #1 priority today!",
                        "TimeFlow", JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        }.execute();
    }
}

private void showGoogleCalendarDialog() {
    String token = JOptionPane.showInputDialog(this,
        "Enter your Google Calendar access token\n" +
        "(Demo mode: type anything to simulate a connection):");

    if (token == null || token.trim().isEmpty()) return;

    boolean connected = calendarService.connect(token);
    if (!connected) {
        JOptionPane.showMessageDialog(this, "Connection failed.");
        return;
    }

    java.util.List<GoogleCalendarService.CalendarEvent> events =
        calendarService.fetchTodaysEvents(calendarApiKey != null ? calendarApiKey : "");

    StringBuilder sb = new StringBuilder("Today's Google Calendar events:\n\n");
    for (GoogleCalendarService.CalendarEvent e : events) {
        sb.append("  ").append(e.toString()).append("\n");
    }
    sb.append("\nImport these into your schedule?");

    int choice = JOptionPane.showConfirmDialog(this,
        sb.toString(), "Google Calendar Import",
        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (choice == JOptionPane.YES_OPTION) {
        for (GoogleCalendarService.CalendarEvent event : events) {
            for (ScheduleItem item : scheduleItems) {
                if (item.getTime().equals(event.time)) {
                    item.setEventName(event.title);
                    item.setType(event.type);
                    item.setNotes("Imported from Google Calendar");
                    break;
                }
            }
        }
        scheduleTableModel.fireTableDataChanged();
        myjTabbedPane.setSelectedIndex(1);
        JOptionPane.showMessageDialog(this,
            events.size() + " events imported into your schedule!");
    }
}

private void deleteSelectedTask() {
    int selectedIndex = jList1.getSelectedIndex();
    if (selectedIndex == -1) {
        JOptionPane.showMessageDialog(this, "Select a task first, then click Delete.");
        return;
    }
    int confirm = JOptionPane.showConfirmDialog(this,
        "Delete task: \"" + dlmTasks.getElementAt(selectedIndex) + "\"?",
        "Confirm Delete", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        taskManager.removeTask(taskManager.getTasks().get(selectedIndex));
        refreshTaskList();
    }
}

private void completeSelectedTask() {
    int selectedIndex = jList1.getSelectedIndex();
    if (selectedIndex == -1) {
        JOptionPane.showMessageDialog(this, "Select a task first.");
        return;
    }
    taskManager.getTasks().get(selectedIndex).markComplete();
    refreshTaskList();
}
    private void showRecipePopup(){
        JTextField nameField = new JTextField(20);
        JTextField ingredientsField = new JTextField(25);
        JTextField prepTimeField = new JTextField(15);
        JTextField categoryField = new JTextField(15);
        JTextField notesField = new JTextField(25);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Recipe Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Ingredients:"));
        panel.add(ingredientsField);
        panel.add(new JLabel("Prep Time:"));
        panel.add(prepTimeField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("Notes:"));
        panel.add(notesField);
        int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Add Recipe",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
            );
        if (result == JOptionPane.OK_OPTION){
           String name = nameField.getText().trim();
           String ingredients = ingredientsField.getText().trim();
           String prepTime = prepTimeField.getText().trim();
           String category = categoryField.getText().trim();
           String notes = notesField.getText().trim();
           if(!name.isEmpty() && !ingredients.isEmpty()){
               recipeManager.addRecipe(name, ingredients, prepTime, category, notes);
               refreshRecipeList();
           }
        }    
    }
    private void showTaskPopup(){
        JTextField taskField = new JTextField(25);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Task:"));
        panel.add(taskField);
        int result = JOptionPane.showConfirmDialog(
                this, panel, "Add Task",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );
        if (result == JOptionPane.OK_OPTION) {
            String taskTitle = taskField.getText().trim();
            if (!taskTitle.isEmpty()){
                taskManager.addTask(taskTitle);
                refreshTaskList();
            }
        }
    }
    private void showLearnPopup() {
        JTextField topicField = new JTextField(25);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Enter Topic:"));
        panel.add(topicField);
        int result = JOptionPane.showConfirmDialog(this,
                panel,
                "Search Learning",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );
        if (result == JOptionPane.OK_OPTION){
            String topic = topicField.getText().trim();
            if (!topic.isEmpty()){
                showYouTubeResultsPopup(topic + "tutorial");
            }
        }
    }
    private void showYouTubeResultsPopup(String query){
        if (apiKey == null || apiKey.trim().isEmpty()){
            specifyApiKey();
            if (apiKey == null || apiKey.trim().isEmpty()){
                JOptionPane.showMessageDialog(this, "No API key set. Can't search YouTube yet.");
                return;
            }
        }
        new SwingWorker<List<YouTubeVideo>, Void>() {
            @Override
            protected List<YouTubeVideo> doInBackground() throws Exception {
                return youtubeClient.searchTopVideos(apiKey, query, 8);
            }
            @Override
            protected void done() {
                try {
                    List<YouTubeVideo> videos = get();
                    DefaultListModel<YouTubeVideo> model = new DefaultListModel<>();
                    for (YouTubeVideo v : videos){
                        model.addElement(v);
                    }
                    JList<YouTubeVideo> resultsList = new JList<>(model);
                    JScrollPane scroll = new JScrollPane(resultsList);
                    int result = JOptionPane.showConfirmDialog(
                        HomeWin.this,
                        scroll,
                        "Top YouTube Results",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                    );
                    if (result == JOptionPane.OK_OPTION){
                        YouTubeVideo selected = resultsList.getSelectedValue();
                        if (selected != null){
                            Desktop.getDesktop().browse(new URI(selected.getUrl()));
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        HomeWin.this,
                        "Couldn't open browser");
                }
            }
        }.execute();
    }
    private void specifyApiKey(){
        String key = JOptionPane.showInputDialog(this, "Paste your YouTube API key here:");
        if (key != null){
            key = key.trim();
                if(!key.isEmpty()){
                    apiKey = key;
                    pref.put(PREF_KEY_FOR_APIKEY, apiKey);
                    JOptionPane.showMessageDialog(this, "API key saved.");
                }
        }
    }
    private void saveAppData(){
        try {
            AppData data = new AppData(
                    taskManager.getTasks(),
                    scheduleItems,
                    recipeItems
            );
            saveManager.saveData(data);
            JOptionPane.showMessageDialog(this, "Saved!");
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error saving: " + ex.getMessage());
        }
    }
    private void loadAppData(){
        try {
        AppData data = saveManager.loadData();
        taskManager.setTasks(data.getTasks());
        scheduleItems = data.getScheduleItems();
        recipeManager.setRecipes(data.getRecipes());
        recipeItems = recipeManager.getRecipes();
        recipeTableModel = new RecipeTableModel(recipeItems);
        recipeTable.setModel(recipeTableModel);
        scheduleTableModel = new ScheduleTableModel(scheduleItems);
        jTable1.setModel(scheduleTableModel);
        refreshTaskList();
        refreshScheduleList();
        refreshRecipeList();
        JOptionPane.showMessageDialog(this, "Loaded!");
        
    } catch (Exception ex){
        JOptionPane.showMessageDialog(this, "Error loading: " + ex.getMessage());
    }
}
    private void clearAppData() {
    java.io.File file = new java.io.File("timeflo-data.ser");

    if (file.exists()) {
        if (file.delete()) {
            JOptionPane.showMessageDialog(this, "Saved data cleared.");
        } else {
            JOptionPane.showMessageDialog(this, "Could not delete saved data.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "No saved data file found.");
    }
}
    private void refreshTaskList(){
        dlmTasks.clear();
        for (Task task : taskManager.getTasks()){
            dlmTasks.addElement(task.toString());
        }
    }
    private void refreshScheduleList(){
        scheduleTableModel.fireTableDataChanged();
    }
    private void refreshRecipeList(){
        recipeTableModel.fireTableDataChanged();    
    }
    private void suggestForRow(int row) {
    ScheduleItem item = scheduleItems.get(row);

    if (!claudeService.hasApiKey()) {
        String key = JOptionPane.showInputDialog(this, "Paste your Claude API key:");
        if (key != null && !key.trim().isEmpty()) {
            claudeService.setApiKey(key.trim());
            pref.put(PREF_KEY_CLAUDE, key.trim());
        }
    }

    // Build context
    StringBuilder scheduleText = new StringBuilder();
    for (ScheduleItem s : scheduleItems) {
        scheduleText.append(s.getTime()).append(" - ")
                    .append(s.getEventName()).append(" (")
                    .append(s.getType()).append(")\n");
    }

    StringBuilder recipeText = new StringBuilder();
    for (Recipe r : recipeItems) {
        recipeText.append("- ").append(r.getName())
                  .append(" (").append(r.getCategory()).append(")\n");
    }

    final String time          = item.getTime();
    final String finalSchedule = scheduleText.toString();
    final String finalRecipes  = recipeText.toString();

    new SwingWorker<String, Void>() {
        @Override
        protected String doInBackground() throws Exception {
            String prompt =
                "You are a scheduling assistant for a college student.\n\n" +
                "Full schedule:\n" + finalSchedule + "\n" +
                "Available recipes:\n" + finalRecipes + "\n\n" +
                "The user clicked on the " + time + " time slot. " +
                "Suggest ONE specific activity for this slot. " +
                "Reply in exactly this format:\n" +
                "EVENT: [name] | TYPE: [School/Meal/Workout/Personal/Work/Open] | NOTE: [short note]\n" +
                "Be specific. If suggesting a meal, pick from the recipe list.";
            return claudeService.getDailyRecommendation(finalSchedule, prompt);
        }
        @Override
        protected void done() {
            try {
                String suggestion = get();
                int choice = JOptionPane.showConfirmDialog(HomeWin.this,
                    "<html><body style='width:300px'>" +
                    "<b>AI suggestion for " + time + ":</b><br><br>" +
                    suggestion.replace("\n", "<br>") +
                    "<br><br>Apply this?</body></html>",
                    "AI Suggestion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE
                );
                if (choice == JOptionPane.YES_OPTION) {
                    // Parse and apply
                    try {
                        String event = suggestion.contains("EVENT:") ?
                            suggestion.split("EVENT:")[1].split("\\|")[0].trim() : suggestion;
                        String type = suggestion.contains("TYPE:") ?
                            suggestion.split("TYPE:")[1].split("\\|")[0].trim() : "Open";
                        String note = suggestion.contains("NOTE:") ?
                            suggestion.split("NOTE:")[1].trim() : "";
                        item.setEventName(event);
                        item.setType(type);
                        item.setNotes(note);
                        scheduleTableModel.fireTableRowsUpdated(row, row);
                    } catch (Exception ex) {
                        // fallback — just put the raw suggestion
                        item.setEventName(suggestion.substring(0, 
                            Math.min(suggestion.length(), 30)));
                        scheduleTableModel.fireTableRowsUpdated(row, row);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(HomeWin.this,
                    "AI suggestion unavailable.", "TimeFlow",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }.execute();
}
    public HomeWin() {
        initComponents();
        saveManager = new AppSaveManager();
        taskManager = new TaskManager();
        scheduleManager = new ScheduleManager();
        recipeManager = new RecipeManager();
        pref = Preferences.userRoot();
        apiKey = pref.get(PREF_KEY_FOR_APIKEY, null);
        if (apiKey == null){
            specifyApiKey();
        } else{
            JOptionPane.showMessageDialog(this, "Loaded your API key.");
        }
        calendarApiKey = pref.get(PREF_KEY_CALENDAR, null);
        calendarService = new GoogleCalendarService();
        String claudeKey = pref.get(PREF_KEY_CLAUDE, null);
        claudeService = new ClaudeAIService(claudeKey != null ? claudeKey : "");
        scheduleItems = new ArrayList<>();
        String[] times = {"8:00", "9:00","10:00","11:00","12:00", "1:00","2:00","3:00","4:00","5:00","6:00","7:00","8:00"};
        for (String t : times){
            scheduleItems.add(new ScheduleItem(t, "Empty", "Open", ""));
        }
        scheduleTableModel = new ScheduleTableModel(scheduleItems);
        jTable1.setModel(scheduleTableModel);
        jTable1.setRowHeight(60);
        jTable1.setFillsViewportHeight(true);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(70); //Time
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(140); //Event
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(90); //Type
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(180); //Notes
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(70);
        jTable1.getColumnModel().getColumn(4).setCellRenderer(
            new com.mycompany.timeflo.model.ButtonRenderer());
        jTable1.getColumnModel().getColumn(4).setCellEditor(
            new com.mycompany.timeflo.model.ButtonEditor(evt -> {
                int row = Integer.parseInt(evt.getActionCommand());
                suggestForRow(row);
            }));
        recipeItems = recipeManager.getRecipes();
        recipeManager.addRecipe("Chicken Bowl", "Chicken, rice, spinach", "20 min", "Dinner", "High protein");
        recipeManager.addRecipe("Oatmeal", "Oats, banana, peanut butter", "5 min", "Breakfast", "Quick meal");
        recipeTableModel = new RecipeTableModel(recipeItems);
        recipeTable.setModel(recipeTableModel);
        refreshRecipeList();
        recipeTable.setRowHeight(60);
        recipeTable.setFillsViewportHeight(true);
        recipeTable.getColumnModel().getColumn(0).setPreferredWidth(110);
        recipeTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        recipeTable.getColumnModel().getColumn(2).setPreferredWidth(70);
        recipeTable.getColumnModel().getColumn(3).setPreferredWidth(90);
        recipeTable.getColumnModel().getColumn(4).setPreferredWidth(140);
        recipeTable.getTableHeader().setReorderingAllowed(false);

        dlmTasks = new DefaultListModel<>();
        taskManager.addTask("Finish CSC lab");
        taskManager.addTask("Study for quiz");
        refreshTaskList();
        jList1.setModel(dlmTasks);

    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        addBtn = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        myjTabbedPane = new javax.swing.JTabbedPane();
        taskToDoPanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        toDoListLbl = new javax.swing.JLabel();
        schedulePanel = new javax.swing.JPanel();
        dayViewPanel = new javax.swing.JPanel();
        weekViewLbl = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        recipesPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        recipeTable = new javax.swing.JTable();
        learnPanel = new javax.swing.JPanel();
        questionTab = new javax.swing.JLabel();
        programmingBtn = new javax.swing.JButton();
        snowboardingBtn = new javax.swing.JButton();
        financeBtn = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        apiKeyBtn = new javax.swing.JMenu();
        mnuSpecifyKey = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        dataMenu = new javax.swing.JMenu();
        SaveMenuItem = new javax.swing.JMenuItem();
        loadMenuItem = new javax.swing.JMenuItem();
        clearMenuItem = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        jLabel2.setText("jLabel2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Timeflo");
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        addBtn.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        addBtn.setText("+");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setText("Journal");
        jButton2.setPreferredSize(new java.awt.Dimension(55, 55));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(112, 112, 112))
        );

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane5.setViewportView(jList1);

        toDoListLbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        toDoListLbl.setText("To-Do List");

        javax.swing.GroupLayout taskToDoPanelLayout = new javax.swing.GroupLayout(taskToDoPanel);
        taskToDoPanel.setLayout(taskToDoPanelLayout);
        taskToDoPanelLayout.setHorizontalGroup(
            taskToDoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(taskToDoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(taskToDoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addGroup(taskToDoPanelLayout.createSequentialGroup()
                        .addComponent(toDoListLbl)
                        .addGap(0, 684, Short.MAX_VALUE)))
                .addContainerGap())
        );
        taskToDoPanelLayout.setVerticalGroup(
            taskToDoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, taskToDoPanelLayout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(toDoListLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        myjTabbedPane.addTab("Tasks", taskToDoPanel);

        weekViewLbl.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        weekViewLbl.setText("Daily Schedule");

        javax.swing.GroupLayout dayViewPanelLayout = new javax.swing.GroupLayout(dayViewPanel);
        dayViewPanel.setLayout(dayViewPanelLayout);
        dayViewPanelLayout.setHorizontalGroup(
            dayViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dayViewPanelLayout.createSequentialGroup()
                .addContainerGap(72, Short.MAX_VALUE)
                .addComponent(weekViewLbl)
                .addGap(81, 81, 81))
        );
        dayViewPanelLayout.setVerticalGroup(
            dayViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dayViewPanelLayout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(weekViewLbl)
                .addContainerGap())
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(jTable1);

        javax.swing.GroupLayout schedulePanelLayout = new javax.swing.GroupLayout(schedulePanel);
        schedulePanel.setLayout(schedulePanelLayout);
        schedulePanelLayout.setHorizontalGroup(
            schedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(schedulePanelLayout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addComponent(dayViewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(schedulePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE))
        );
        schedulePanelLayout.setVerticalGroup(
            schedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(schedulePanelLayout.createSequentialGroup()
                .addComponent(dayViewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        myjTabbedPane.addTab("Schedule", schedulePanel);

        recipeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(recipeTable);

        javax.swing.GroupLayout recipesPanelLayout = new javax.swing.GroupLayout(recipesPanel);
        recipesPanel.setLayout(recipesPanelLayout);
        recipesPanelLayout.setHorizontalGroup(
            recipesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 778, Short.MAX_VALUE)
        );
        recipesPanelLayout.setVerticalGroup(
            recipesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, recipesPanelLayout.createSequentialGroup()
                .addGap(0, 9, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        myjTabbedPane.addTab("Recipes", recipesPanel);

        questionTab.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        questionTab.setText("What do you want to learn about?");

        programmingBtn.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        programmingBtn.setText("Programming");
        programmingBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programmingBtnActionPerformed(evt);
            }
        });

        snowboardingBtn.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        snowboardingBtn.setText("Snowboarding");
        snowboardingBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                snowboardingBtnActionPerformed(evt);
            }
        });

        financeBtn.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        financeBtn.setText("Finance");
        financeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                financeBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout learnPanelLayout = new javax.swing.GroupLayout(learnPanel);
        learnPanel.setLayout(learnPanelLayout);
        learnPanelLayout.setHorizontalGroup(
            learnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(learnPanelLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(learnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(questionTab)
                    .addGroup(learnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(financeBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(programmingBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(snowboardingBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(357, Short.MAX_VALUE))
        );
        learnPanelLayout.setVerticalGroup(
            learnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(learnPanelLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(questionTab, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(programmingBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(snowboardingBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(financeBtn)
                .addContainerGap(142, Short.MAX_VALUE))
        );

        myjTabbedPane.addTab("Learn", learnPanel);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(myjTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 778, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(myjTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jButton1.setText("jButton1");

        apiKeyBtn.setText("API");

        mnuSpecifyKey.setText("Specify API Key");
        mnuSpecifyKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSpecifyKeyActionPerformed(evt);
            }
        });
        apiKeyBtn.add(mnuSpecifyKey);

        jMenuItem2.setText("Connect Google Calendar");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        apiKeyBtn.add(jMenuItem2);

        jMenuBar1.add(apiKeyBtn);

        dataMenu.setText("Data");

        SaveMenuItem.setText("Save");
        SaveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveMenuItemActionPerformed(evt);
            }
        });
        dataMenu.add(SaveMenuItem);

        loadMenuItem.setText("Load");
        loadMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadMenuItemActionPerformed(evt);
            }
        });
        dataMenu.add(loadMenuItem);

        clearMenuItem.setText("Clear");
        clearMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearMenuItemActionPerformed(evt);
            }
        });
        dataMenu.add(clearMenuItem);

        jMenuBar1.add(dataMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1357, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 284, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 283, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 690, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 96, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 97, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
                                     
        private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
            int selectedTab = myjTabbedPane.getSelectedIndex();
            if (selectedTab == 0) {
                String[] options = {"Add Task", "Delete Selected", "Mark Complete"};
                int choice = JOptionPane.showOptionDialog(this,
                    "What would you like to do?", "Tasks",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);
                if (choice == 0) showTaskPopup();
                else if (choice == 1) deleteSelectedTask();
                else if (choice == 2) completeSelectedTask();
            } else if (selectedTab == 1) {
                showSchedulePopup();
            } else if (selectedTab == 2) {
                showRecipePopup();
            } else if (selectedTab == 3) {
                showLearnPopup();  
            
    }                                      
    }//GEN-LAST:event_addBtnActionPerformed

    private void programmingBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programmingBtnActionPerformed
        showYouTubeResultsPopup("java programming tutorial beginner");
    }//GEN-LAST:event_programmingBtnActionPerformed

    private void snowboardingBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_snowboardingBtnActionPerformed
        showYouTubeResultsPopup("snowboarding beginner tutorial");
    }//GEN-LAST:event_snowboardingBtnActionPerformed

    private void financeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_financeBtnActionPerformed
        showYouTubeResultsPopup("personal finance basics for college students");
    }//GEN-LAST:event_financeBtnActionPerformed

    private void mnuSpecifyKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSpecifyKeyActionPerformed
        specifyApiKey();
    }//GEN-LAST:event_mnuSpecifyKeyActionPerformed

    private void loadMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadMenuItemActionPerformed
        loadAppData();
    }//GEN-LAST:event_loadMenuItemActionPerformed

    private void SaveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveMenuItemActionPerformed
        saveAppData();
    }//GEN-LAST:event_SaveMenuItemActionPerformed

    private void clearMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearMenuItemActionPerformed
        clearAppData();
    }//GEN-LAST:event_clearMenuItemActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        showGoogleCalendarDialog();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        showJournalPopup();
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem SaveMenuItem;
    private javax.swing.JButton addBtn;
    private javax.swing.JMenu apiKeyBtn;
    private javax.swing.JMenuItem clearMenuItem;
    private javax.swing.JMenu dataMenu;
    private javax.swing.JPanel dayViewPanel;
    private javax.swing.JButton financeBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList<String> jList1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel learnPanel;
    private javax.swing.JMenuItem loadMenuItem;
    private javax.swing.JMenuItem mnuSpecifyKey;
    private javax.swing.JTabbedPane myjTabbedPane;
    private javax.swing.JButton programmingBtn;
    private javax.swing.JLabel questionTab;
    private javax.swing.JTable recipeTable;
    private javax.swing.JPanel recipesPanel;
    private javax.swing.JPanel schedulePanel;
    private javax.swing.JButton snowboardingBtn;
    private javax.swing.JPanel taskToDoPanel;
    private javax.swing.JLabel toDoListLbl;
    private javax.swing.JLabel weekViewLbl;
    // End of variables declaration//GEN-END:variables
}

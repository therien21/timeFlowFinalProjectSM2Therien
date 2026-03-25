/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.timeflo;

import com.mycompany.timeflo.model.YouTubeVideo;
import com.mycompany.timeflo.service.YouTubeClient;
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
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.SwingWorker;
import java.util.prefs.Preferences;
import com.mycompany.timeflo.manager.TaskManager;
import com.mycompany.timeflo.model.Task;
import com.mycompany.timeflo.manager.ScheduleManager;
import com.mycompany.timeflo.model.ScheduleItem;
import com.mycompany.timeflo.manager.RecipeManager;
import com.mycompany.timeflo.model.Recipe;

/**
 *
 * @author theri
 */
public class HomeWin extends javax.swing.JFrame {

    private DefaultListModel<String> dlmScheduleTimes;
    private DefaultListModel<String> dlmScheduleEvents;
    private DefaultListModel<String> dlmRecipeNames;
    private DefaultListModel<String> dlmRecipeIngredients;
    private DefaultListModel<String> dlmTasks;
    private TaskManager taskManager;
    private ScheduleManager scheduleManager;
    private RecipeManager recipeManager;
    private final YouTubeClient youtubeClient = new YouTubeClient();
    private String apiKey;
    private Preferences pref;
    private static final String PREF_KEY_FOR_APIKEY = "TIMEFLO_YOUTUBE_API_KEY";
    
    private void showSchedulePopup(){
        String[] times = {"8:00", "9:00", "10:00", "11:00", "12:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00"};
        JComboBox<String> timeBox = new JComboBox<>(times);
        JTextField eventField = new JTextField(25);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Select Time:"));
        panel.add(timeBox);
        panel.add(new JLabel("Enter Event:"));
        panel.add(eventField);
        int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Add Schedule Item",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
            );
        if (result == JOptionPane.OK_OPTION){
            String time = (String) timeBox.getSelectedItem();
            String eventName = eventField.getText().trim();
            if (!eventName.isEmpty()){
                boolean updated = scheduleManager.updateScheduleEvent(time, eventName);
                if(updated){
                    refreshScheduleList();
                } else{
                    JOptionPane.showMessageDialog(this, "That time was not found.");
                }
            } 
           
        }
    }
    private void showRecipePopup(){
        JTextField nameField = new JTextField(10);
        JTextField ingredientsField = new JTextField(25);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Recipe Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Ingredients:"));
        panel.add(ingredientsField);
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
           if(!name.isEmpty() && !ingredients.isEmpty()){
               recipeManager.addRecipe(name, ingredients);
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
    private void refreshTaskList(){
        dlmTasks.clear();
        for (Task task : taskManager.getTasks()){
            dlmTasks.addElement(task.toString());
        }
    }
    private void refreshScheduleList(){
        dlmScheduleTimes.clear();
        dlmScheduleEvents.clear();
        for (ScheduleItem item : scheduleManager.getScheduleItems()){
            dlmScheduleTimes.addElement(item.getTime());
            dlmScheduleEvents.addElement(item.getEventName());
        }
    }
    private void refreshRecipeList(){
        dlmRecipeNames.clear();
        dlmRecipeIngredients.clear();
        for (Recipe recipe : recipeManager.getRecipes()){
            dlmRecipeNames.addElement(recipe.getName());
            dlmRecipeIngredients.addElement(recipe.getIngredients());
        }
    }
    public HomeWin() {
        initComponents();
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
        dlmScheduleTimes = new DefaultListModel<>();
        dlmScheduleEvents = new DefaultListModel<>();
        String[] times = {"8:00", "9:00","10:00", "11:00","12:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00" };
        for (String t : times){
            scheduleManager.addScheduleItem(t, "");
        }
        refreshScheduleList();
        timeJList.setModel(dlmScheduleTimes);
        scheduleJList.setModel(dlmScheduleEvents);
        dlmRecipeNames = new DefaultListModel<>();
        dlmRecipeIngredients = new DefaultListModel<>();
        recipeManager.addRecipe("Recipe 1", "Ingredient A");
        recipeManager.addRecipe("Recipe 2", "Ingredient B");
        recipeNameJList.setModel(dlmRecipeNames);
        refreshRecipeList();
        recipeIngredientsJList.setModel(dlmRecipeIngredients);
        
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

        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        addBtn = new javax.swing.JButton();
        myjTabbedPane = new javax.swing.JTabbedPane();
        taskToDoPanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        toDoListLbl = new javax.swing.JLabel();
        schedulePanel = new javax.swing.JPanel();
        dayViewPanel = new javax.swing.JPanel();
        weekViewLbl = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        scheduleJList = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        timeJList = new javax.swing.JList<>();
        recipesPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        recipeNameJList = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        recipeIngredientsJList = new javax.swing.JList<>();
        learnPanel = new javax.swing.JPanel();
        questionTab = new javax.swing.JLabel();
        programmingBtn = new javax.swing.JButton();
        snowboardingBtn = new javax.swing.JButton();
        financeBtn = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        mnuSpecifyKey = new javax.swing.JMenuItem();

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addGap(0, 367, Short.MAX_VALUE)))
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

        scheduleJList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(scheduleJList);

        timeJList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "8:00", "9:00", "10:00", "11:00", "12:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(timeJList);

        javax.swing.GroupLayout schedulePanelLayout = new javax.swing.GroupLayout(schedulePanel);
        schedulePanel.setLayout(schedulePanelLayout);
        schedulePanelLayout.setHorizontalGroup(
            schedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(schedulePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(schedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dayViewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        schedulePanelLayout.setVerticalGroup(
            schedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(schedulePanelLayout.createSequentialGroup()
                .addComponent(dayViewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(schedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                    .addComponent(jScrollPane4))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        myjTabbedPane.addTab("Schedule", schedulePanel);

        recipeNameJList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(recipeNameJList);

        recipeIngredientsJList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(recipeIngredientsJList);

        javax.swing.GroupLayout recipesPanelLayout = new javax.swing.GroupLayout(recipesPanel);
        recipesPanel.setLayout(recipesPanelLayout);
        recipesPanelLayout.setHorizontalGroup(
            recipesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recipesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                .addContainerGap())
        );
        recipesPanelLayout.setVerticalGroup(
            recipesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recipesPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(recipesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addContainerGap(127, Short.MAX_VALUE))
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
                .addContainerGap(40, Short.MAX_VALUE))
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
            .addComponent(myjTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        mnuSpecifyKey.setText("Specify API Key");
        mnuSpecifyKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSpecifyKeyActionPerformed(evt);
            }
        });
        jMenu2.add(mnuSpecifyKey);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 461, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 497, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        int selectedTab = myjTabbedPane.getSelectedIndex();
        if (selectedTab == 0){
            showTaskPopup();
        } else if (selectedTab == 1){
            showSchedulePopup();
        } else if (selectedTab == 2){
            showRecipePopup();
        } else if (selectedTab == 3){
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
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JPanel dayViewPanel;
    private javax.swing.JButton financeBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JPanel learnPanel;
    private javax.swing.JMenuItem mnuSpecifyKey;
    private javax.swing.JTabbedPane myjTabbedPane;
    private javax.swing.JButton programmingBtn;
    private javax.swing.JLabel questionTab;
    private javax.swing.JList<String> recipeIngredientsJList;
    private javax.swing.JList<String> recipeNameJList;
    private javax.swing.JPanel recipesPanel;
    private javax.swing.JList<String> scheduleJList;
    private javax.swing.JPanel schedulePanel;
    private javax.swing.JButton snowboardingBtn;
    private javax.swing.JPanel taskToDoPanel;
    private javax.swing.JList<String> timeJList;
    private javax.swing.JLabel toDoListLbl;
    private javax.swing.JLabel weekViewLbl;
    // End of variables declaration//GEN-END:variables
}

# StickToYourBudget

This is a project for my MSc Android Development course and my first app using Android SDK.

It's an expense tracking app that allows user to add expenses, inspect them in a list and delete them, search for expenses 
in specific dates and add new categories to the predifined ones.

The app uses a tab layout for main functions with swipe views (based on this awesome tutorial that you can find here 
http://www.truiton.com/2015/06/android-tabs-example-fragments-viewpager/) and each function is emplemented as a fragment.
Tabs "Inspection" and "Analysis" use a pretty ListView with custom Adapter that you can find in this awesome tutorial right here 
http://www.journaldev.com/10416/android-listview-with-custom-adapter-example-tutorial.

I also used google maps API to track user's location when an expense is added but API key has been removed in order to share my code publicly.
Please fill in your own API key in google_maps_api.xml file, located in src/debug/res/values.

**Known bugs: When the user adds a new category, in order to be listed in spinner (Expenses Tab), you have to switch first 
to Inspection Tab and then again to Expenses.Same thing for expense deletion in Inspection Tab.When delete is pressed, the
expenses is being remove from database but the list don't repopulate unless the user switches to Expense Tab and back.It has to
do with app states (Resume-Paused) but haven't figured out how to solve this yet.

Note to Self: A preview of the app will be added in next commmit!

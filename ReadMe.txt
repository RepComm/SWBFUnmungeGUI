Updated for swbf-unmunge v0.5.0 on 7/28/17

What is it:
 - A simple GUI for SleepKiller's SWBF Unmunge/decompiler tool.

INSTRUCTIONS:
 If you don't have java yet, you'll need it:
 - Oracle Java :https://www.java.com/en/download/ (Java Runtime Environment JRE)

 If you don't have swbf-unmunge tool, download the latest build here:
 - https://github.com/SleepKiller/swbf-unmunge/releases
 
 If you're on windows, you should be able to double click SWBFUnmungeUI.jar
 
 On any platform, you can run this command from a command terminal:
 java -jar "SWBFUnmungeUI.jar"
 
 (Optionally) Again on windows:
 You could go to the directory containing the jar itself,
 Click in the address bar of your file explorer, and paste that same command.
 Command terminal should open and automatically navigate to the folder, and run it.

 1. After the GUI opens, it will try to find 'swbf-unmunge.exe' in the same
  directory that the SWBFUnmungeGUI.jar is in.
  If it can't find it there, a message should display in the lowest text area,
  saying "Couldn't find unmunge: Drag And Drop swbf-unmunge.exe onto the Drop Zone"
  
  Simply find the swbf-unmuge.exe file, and drag it onto the drop zone, the message
  should disappear (unless it doesn't have priviledges to run it, of course).
  
 2. Drag any *.LVL file onto the 'Drop Zone', and see it appear in the file list.
  You can change the options for output using the controls.
  
 3. Run or get command list:
  If the gui has priviledges, you can hit the "Run" button, if it can't,
  you can try "Get Commands" button.
  
 The tool should send some data to the lowest text area in your GUI.
  The generated files are in the directory as the *.LVL file they're from.
 
How is it:
 - Functional, not finished yet: File browser isn't hooked up completely yet

Where is it:
 - Forum Topic: http://www.swbfgamers.com/index.php?topic=12158.0
 - Source/Project: https://github.com/SleepKiller/swbf-unmunge/releases
 - Releases: https://github.com/RepComm/SWBFUnmungeGUI/releases

Why *is* it:
 - SK's work is awesome, and I hope that a GUI will let people
   be less afraid of the process, or at least look pretty.
 
Features:
 - File Drag'n'Drop
 - File queue (you can add multiple files)
 - Tool output sent directly to gui
 - Cmd windows are hidden
 - Error handling
 - GUI option controls

Not implemented (fully) yet:
 - File browser (its acting funny)
 - Duplicate file checking

echo off 

rem This software packs the content of the folder it is executed from
rem (files, folders and subfolders) in a ZIP file.

rem You can exclude files and folders listing them in as arguments 
rem (in place of "exclude"). Names of the files to be excluded should
rem be listed with extensions (.txt, .pdf etc.).

rem The content of the result ZIP file include
rem the original jar file: FolderBackupTool_vXX.jar
rem and the bat file bat FolderBackupTool_vXX.bat

rem The name of the ZIP file is composed of:
rem FOLDERNAME + _backup_ + dateandtime + .zip

rem Options to copy/move the result ZIP file to a another folder
rem given as an argument (along with excluded folders/files)
rem can be added later if needed.



java -jar FolderBackupTool_v0.3.jar .git release target images
Set WinScriptHost = CreateObject("WScript.Shell")
WinScriptHost.Run Chr(34) & "jre\start_POST.bat" & Chr(34), 0, true
'WScript.sleep 1000
Set WinScriptHost = Nothing

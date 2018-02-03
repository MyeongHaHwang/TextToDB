# TextToDB

Language = Java

Function = TXT -> Preprocessing -> Database (SQL)

Files = ITU-T Recommendations (International Standards Documents of International Telecommunication Union)

1.Load the file directory

2.Cut the part of the file name (ex/ T-REC-Y.3500-201408-I!!PDF-E.txt)

3.Set the strings to input Database

 - 1. original file name
 
 - 2. series id (ex/ Y.3500)
  
 - 3. series name (ex/ Y)
  
 - 4. series number (ex/ 3500)
  
 - 5. date (ex/ 201408)
  
 - 6. year (ex/ 2014)
  
 - 7. contents (contents of each file after preprocessing process)
  
 - 7-1. preprocessing : slicing / convert comma("'") to space / delete the stopwords
  
Developer Contact: hmh929@kaist.ac.kr

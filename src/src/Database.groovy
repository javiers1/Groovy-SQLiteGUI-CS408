package src
   import java.sql.*
   import org.sqlite.SQLite
   import groovy.sql.Sql
	
   /**
    * Takes in a Database name as a String in the Constructor and attempts to connect
    * To that databse. The database file must be in the src files, if not, a new .db
    * file will be created.
   	*
   	* Example of a Groovy Class
    */
   class SQLLiteDatabase {
	   
   private def sql 
	
   SQLLiteDatabase(String databaseName){
	   try {
		   	sql = Sql.newInstance("jdbc:sqlite:$databaseName", "org.sqlite.JDBC")
	   } catch (Exception e) {
			e.printStackTrace()
	   }
	   
   		println("Successfully Connected to $databaseName !")
	   
   }
   
   /**
    * Adds a Row to a Table.
    * 
    * Parameters:
    * String tableName: the name of the table in the Database
    * Map: <String,Object> where String is the Column Name, and Object is it corresponding value
    * 
    * Example: add("Course", [CourseN:10, Coursename:"NewCourse", Nunit: 6])
    * 
    * Tablename is Course, CourseN = 10, Coursename = NewCourse, Nunit = 6
    */
   public add(String tableName, Map<String,Object> map){
	   try{
		   def table = sql.dataSet(tableName)
		   table.add(map)
	   } catch (Exception e) {
	   		e.printStackTrace()
	   }
	   
   }
  
   /**
    * Prints the contents of the Table name
    * @param tableName name of table
    */
   public def printcontents(String tableName) {
	   def rows = []
	   try {
		   sql.eachRow('Select * from ' + tableName) {
				   println(it.toString())
			   }
	   } catch (Exception e) {
			   e.printStackTrace()
	   }
   }  
	   
  
   /**
	* Returns an Array of all the Rows in a specified table
	* @param String tableName
	* @return an Array With table information
	*/
   public def getTableRows(String tableName){
	   def rows = []
	   try { 
		   sql.eachRow('Select * from ' + tableName) {
		   		rows << it.toRowResult()
	   		}
	   } catch (Exception e) {
	   		e.printStackTrace()
	   }
	   rows
   }
   
   
   }
   
  
   
   def databse = new SQLLiteDatabase("Students.db")
   
   //databse.add("Course", [CourseN:10, Coursename:"NewCourse", Nunit: 6])
   
   println(databse.getTableRows("Course"))
   
   println()
   
   databse.printcontents("Course")
   
   // ------------------------------ NOTES ------------------------
   //sql.execute("drop table if exists person")
   //sql.execute("create table person (id integer, name string)")
	
   //def people = sql.dataSet("person")
   //people.add(id:1, name:"leo")
   //people.add(id:2,name:'yui')
   //people.add(name: "javi",id:4)
   
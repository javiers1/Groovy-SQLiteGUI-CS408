package src
   import java.sql.*
   import org.sqlite.SQLite
import groovy.sql.GroovyRowResult
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
   public def printContentsWithColumnNames(String tableName) {
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
	* Returns a List of all the Rows in a specified table with column name.
	* @param String tableName
	* 
	* @return an Array of RowResultObjects. A RowResultObject is basically a <Key, Value> map,
	* where the Key is the Column Name, and the Value is the Object
	* 
	* i.e. [Student: javier, ID: 2564]
	*/
   public def getTableRowsWithColumnName(String tableName){
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
   
   /**
	* Returns a List of all the Rows in a specified table without column name.
	* @param String tableName
	*
	* @return an Array of RowResultObjects. A RowResultObject is basically a <Key, Value> map,
	* where the Key is the Column Name, and the Value is the Object
	*
	* i.e. [Student: javier, ID: 2564]
	*/
   public def getTableRowsWithoutColumnName(String tableName){
	   def rows = []
	   try {
		   sql.eachRow('Select * from ' + tableName) {
				   rows << it.toRowResult()
			   }
	   } catch (Exception e) {
			   e.printStackTrace()
	   }
	   def result = []
	   for(int i = 0; i < rows.size(); i++){
		   result << new ArrayList()
	   }
	   int j = 0
	   for(GroovyRowResult rowObject: rows){
		   for(int i = 0; i < rowObject.size(); i++){
			   result.get(j).add(rowObject.getAt(i))
		   }
		   j++
	   }
	   result
   }
   
   
   
   /**
    * Returns the table column headers as an Array for the specified tableName
    * @param tableName
    * @return
    */
   public def getTableHeaders(String tableName){
	   def aRow;
	   try { 
		   aRow = sql.firstRow('Select * from ' + tableName)
	   } catch (Exception e) {
	   		e.printStackTrace()
	   }
	   def headers;
	   headers = new ArrayList(aRow.keySet())
	   headers
   }
   
   
}
   
   def databse = new SQLLiteDatabase("Students.db")
   
   //databse.add("Course", [CourseN:10, Coursename:"NewCourse", Nunit: 6])
   
   println(databse.getTableRowsWithColumnName("Course"))
   
   println()
   
   println(databse.getTableRowsWithoutColumnName("Course"))
   
   println()
   
   databse.printContentsWithColumnNames("Course")
   
   println()
   
   databse.printContentsWithColumnNames("Student")
      
   println()
   
   println(databse.getTableHeaders("Course"))
   // ------------------------------ NOTES ------------------------
   //sql.execute("drop table if exists person")
   //sql.execute("create table person (id integer, name string)")
	
   //def people = sql.dataSet("person")
   //people.add(id:1, name:"leo")
   //people.add(id:2,name:'yui')
   //people.add(name: "javi",id:4)
   
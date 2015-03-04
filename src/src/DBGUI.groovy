package src

import groovy.swing.SwingBuilder
import groovy.beans.Bindable
import static javax.swing.JFrame.EXIT_ON_CLOSE
import java.awt.*
import javax.swing.*
 
@Bindable
class Query{
    String select, from, where
	String toString(){ 
		"SELECT $select FROM $from WHERE $where;"
    }
}
@Bindable
class Add{
	String CourseN, CourseName, Nunit
	String toString(){
		"Course $CourseN $CourseName $Nunit"
	}
}
@Bindable
class Delete{
	String CourseN, CourseName, Nunit
	String toString(){
		"$CourseN $CourseName $Nunit"
	}
}

class DBController{
	def myDatabase = new SQLLiteDatabase("Students.db")
	def data = myDatabase.getTableRowsWithColumnName("Course")
	void rowAdd(Add row){
		myDatabase.add("Course", [CourseN: row.getCourseN().toInteger(), CourseName: row.getCourseName(),
		Nunit: row.getNunit().toInteger()])
		data = myDatabase.getTableRowsWithColumnName("Course")
		databaseTable()
	}
	void rowDelete(Delete row){
		myDatabase.delete(row.getCourseN().toInteger())
		data = myDatabase.getTableRowsWithColumnName("Course")
		databaseTable()
	}
	void databaseTable(){
		def query = new Query(select: '*', from: 'Course', where: 'Nunit >= 4')
		Font font = new Font("PLAIN",Font.PLAIN,18)
		def swingBuilder = new SwingBuilder()
		swingBuilder.edt {
			lookAndFeel 'nimbus'
			frame = swingBuilder.frame(title: 'SQL Query', size: [900, 600],
			show: true, locationRelativeTo: null,
			defaultCloseOperation: EXIT_ON_CLOSE) {
				borderLayout(vgap: 5)
				panel(constraints: BorderLayout.WEST,
				border: compoundBorder([emptyBorder(15), titledBorder('SQL Query:')])) {
					tableLayout {
						tr {
							td {
								label ('  SELECT :').setFont(font)
							}
							td {
								textField query.select, id: 'selectField', columns: 15
							}
						}
						tr {
							td {
								label ('   FROM :').setFont(font)
							}
							td {
								textField id: 'fromField', columns: 15, query.from
							}
						}
						tr {
							td {
								label ('   WHERE :').setFont(font)
							}
							td {
								textField id: 'whereField', columns: 15, query.where
							}
						}
						tr{
							td{
								label('\n')
							}
						}
						tr{
							td{
								button text: 'Add a Row', actionPerformed:{
									frame.show(false)
									addRowTable()
								}
							}
							td{
								button text: 'Delete a Row', actionPerformed:{
									frame.show(false)
									deleteRowTable()
								}
							}
						}
					}
				}
				panel(constraints: BorderLayout.CENTER,
				border: compoundBorder([emptyBorder(15), titledBorder('Database File:')])){
				   scrollPane{
					   table{
						   tableModel(list:data){
							   propertyColumn(header:'CourseN',propertyName:'CourseN')
							   propertyColumn(header:'CourseName',propertyName: 'CourseName')
							   propertyColumn(header:'Nunit',propertyName: 'Nunit')
						   }
					   }
				   }
				}
				panel(constraints: BorderLayout.SOUTH){
					button text: 'Submit query', actionPerformed: {
						query.setSelect(selectField.text)
						query.setFrom(fromField.text)
						query.setWhere(whereField.text)
						queryResult(query);
					}
				}
			}
		}
	}
	void queryResult(Query query){
		def data2 = myDatabase.query(query.toString())
		Font font = new Font("PLAIN",Font.PLAIN,18)
		def swingBuilder = new SwingBuilder()
		swingBuilder.edt {
			lookAndFeel 'nimbus'
			frame = swingBuilder.frame(title: 'Query Result', size: [700, 600],
			show: true, locationRelativeTo: null,
			defaultCloseOperation: EXIT_ON_CLOSE){
				borderLayout(vgap: 5)
				panel(constraints: BorderLayout.CENTER,
				border: compoundBorder([emptyBorder(15), titledBorder('Database File:')])){
				   scrollPane{
					   table{
						   tableModel(list:data2){
							   propertyColumn(header:'CourseN',propertyName:'CourseN')
							   propertyColumn(header:'CourseName',propertyName: 'CourseName')
							   propertyColumn(header:'Nunit',propertyName: 'Nunit')
						   }
					    }
				    }
				}
				panel(constraints: BorderLayout.SOUTH){
					button text: 'Okay', actionPerformed: {
						frame.show(false)
					}
				}
			}
		}
	}
	void addRowTable(){
		def add = new Add(CourseN: '408', CourseName: 'Programming Languages', Nunit: '4')
		Font font = new Font("PLAIN",Font.PLAIN,18)
		def swingBuilder = new SwingBuilder()
		swingBuilder.edt {
			lookAndFeel 'nimbus'
			frame = swingBuilder.frame(title: 'Add a Row', size: [400, 300],
			show: true, locationRelativeTo: null,
			defaultCloseOperation: EXIT_ON_CLOSE){
				borderLayout(vgap: 5)
				panel(constraints: BorderLayout.CENTER,
				border: compoundBorder([emptyBorder(15), titledBorder('')])) {
					tableLayout {
						tr {
							td {
								label ('  Course Number :').setFont(font)
							}
							td {
								textField add.CourseN, id: 'coursenField', columns: 15
							}
						}
						tr {
							td {
								label ('   Course Name :').setFont(font)
							}
							td {
								textField add.CourseName, id: 'coursenameField', columns: 15
							}
						}
						tr {
							td {
								label ('   Number of Units :').setFont(font)
							}
							td {
								textField add.Nunit, id: 'unitsField', columns: 15
							}
						}
						tr{
							td{
								label('\n')
							}
						}
					}
				}
				panel(constraints: BorderLayout.SOUTH){
					button text: 'Add Row', actionPerformed: {
						add.setCourseN(coursenField.text)
						add.setCourseName(coursenameField.text)
						add.setNunit(unitsField.text)
						println add
						rowAdd(add);
						frame.show(false)
					}
				}
			}
		}
	}
	void deleteRowTable(){
		def delete = new Delete(CourseN: '408', CourseName: 'Programming Languages', Nunit: '4')
		Font font = new Font("PLAIN",Font.PLAIN,18)
		def swingBuilder = new SwingBuilder()
		swingBuilder.edt {
			lookAndFeel 'nimbus'
			frame = swingBuilder.frame(title: 'Delete a Row', size: [400, 300],
			show: true, locationRelativeTo: null,
			defaultCloseOperation: EXIT_ON_CLOSE){
				borderLayout(vgap: 5)
				panel(constraints: BorderLayout.CENTER,
				border: compoundBorder([emptyBorder(15), titledBorder('')])) {
					tableLayout {
						tr {
							td {
								label ('  Course Number :').setFont(font)
							}
							td {
								textField delete.CourseN, id: 'coursenField', columns: 15
							}
						}
						tr{
							td{
								label('\n')
							}
						}
					}
				}
				panel(constraints: BorderLayout.SOUTH){
					button text: 'Delete Row', actionPerformed: {
						delete.setCourseN(coursenField.text)
						rowDelete(delete)
						frame.show(false)	
					}
				}
			}
		}
	}
}

def driver = new DBController()
driver.databaseTable()
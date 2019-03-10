<?php
 	
 	require "db_config.php";

 	if($_SERVER['REQUEST_METHOD']=='GET'){

 		$budget_category = $_GET['budget_category'];
 		$category = "";

 		if($budget_category == 0){
 			$category = "DAY";
 		}else if($budget_category == 1){
 			$category = "WEEK";
 		}else{
 			$category = "MONTH";
 		}
	 	
	 	$query = "SELECT expense.expense_id, expense.expense_name, expense.budget_id, expense.category_name, expense.expense_date, expense.expense_amount FROM `expense` 
				WHERE `expense`.`expense_date` >= SUBDATE(CURDATE(), INTERVAL 1 $category)";
	 
	 	$res = mysqli_query($con,$query);
	 	
		$result = array();
		
		while($row = mysqli_fetch_array($res)){
			array_push($result,
				array('expense_id'=>$row[0],
				'expense_name'=>$row[1],
				'budget_id'=>$row[2],
				'category_name'=>$row[3],
				'expense_date'=>$row[4],
				'expense_amount'=>$row[5],
				"status"=>'success'
			));
		}
		 
		echo json_encode(array("result"=>$result));
		 
		mysqli_close($con);
	 
	 }
?>

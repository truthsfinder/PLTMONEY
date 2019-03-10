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
	 	
	 	//$query = "SELECT SUM(expense.expense_amount) as total FROM `expense` 
		//		LEFT JOIN budget ON expense.budget_id = budget.budget_id
		//		WHERE budget.budget_category = '$category' AND budget.budget_status = 'active'";
		$query = "SELECT SUM(expense.expense_amount) as total FROM `expense` WHERE `expense`.`expense_date` >= SUBDATE(CURDATE(), INTERVAL 1 $category)";
	 
	 	$res = mysqli_query($con,$query);
	 	$row = mysqli_fetch_array($res);
	 	
		if($row != NULL){
			echo $row['total'];
		}else{
			echo 0;
		}
	 }
?>

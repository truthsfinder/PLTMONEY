<?php
 	
 	require "db_config.php";

 	if($_SERVER['REQUEST_METHOD']=='GET'){

 		$user_id = $_GET['user_id'];
	 	
	 	//get budget
	 	$query = "SELECT * FROM `budget` 
	 		WHERE `user_id` = $user_id AND
	 		budget_category = 'Weekly' AND
			budget_status = 'active'";
	 
	 	$res = mysqli_query($con,$query);
		$row = mysqli_fetch_array($res);

		if($row){
			//get expenses
			//$query2 = "SELECT SUM(expense.expense_amount) as total_expense FROM `expense` 
		 	//	WHERE `expense`.`budget_id` = " . $row['budget_id'];

		 	$query2 = "SELECT SUM(expense.expense_amount) as total_expense FROM `expense` 
		 		WHERE `expense`.`expense_date` >= SUBDATE(CURDATE(), INTERVAL 1 WEEK) ";

		 	$res2 = mysqli_query($con,$query2);
			$row2 = mysqli_fetch_array($res2);

			if($row2){
				$result = $row['budget_amount'] - $row2['total_expense'];
			 
				echo json_encode($result);
			}else{
				echo json_encode(0);
			}
		}else{
			echo json_encode(0);
		}
		mysqli_close($con);
	 
	 }
?>

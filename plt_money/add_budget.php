<?php
 	
 	require "db_config.php";

 	//inserting data to user
	$budget_amount = $_POST["budget_amount"];
	$budget_category = $_POST["budget_category"];

	$user_id = $_GET['user_id'];

	$query1 = "UPDATE `budget` SET budget_status = 'closed'
	 		WHERE `user_id` = $user_id AND
	 		budget_category = '$budget_category' AND
			budget_status = 'active'";

	mysqli_query($con, $query1);

	$query2 = "INSERT INTO budget (`user_id`, `budget_amount`, `budget_category`, `budget_status`) 
			  VALUES($user_id, $budget_amount, '$budget_category', 'active');";
	
	if(mysqli_query($con, $query2)){
		echo "success";
	}else{
		echo "failed";
	}

?>
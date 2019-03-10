<?php
 	
 	require "db_config.php";

 	//inserting data to expense
	$expense_name = $_POST["expense_name"];
	$expense_category = $_POST["category_name"];
	$expense_amount = $_POST["expense_amount"];
	$expense_date = $_POST["expense_date"];

	$budget_id = $_GET['budget_id'];
	
	$query = "INSERT INTO expense (`budget_id`, `expense_name`, `category_name`, `expense_date`, `expense_amount`, `expense_status`) 
			  VALUES($budget_id, '$expense_name', '$expense_category', '$expense_date', '$expense_amount', 'active');";


	if(mysqli_query($con, $query)){
		echo "success";
	}else{
		echo "failed";
	}

?>
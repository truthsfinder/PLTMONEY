<?php
 	
 	require "db_config.php";

 	//update category
	$expense_id = $_GET['expense_id'];

	$expense_name = $_POST['expense_name'];
	$expense_date = $_POST['expense_date'];
	$expense_amount = $_POST['expense_amount'];
	$category_name = $_POST['expense_category'];

	$query = "UPDATE expense SET expense_name = '$expense_name', category_name = '$category_name', expense_date = '$expense_date', expense_amount = $expense_amount WHERE expense_id = $expense_id";
	
	if(mysqli_query($con, $query)){
		echo "success";
	}else{
		echo "failed";
	}

?>
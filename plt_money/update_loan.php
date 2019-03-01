<?php
 	
 	require "db_config.php";

 	//update category
	$loan_id = $_GET['loan_id'];

	$loan_borrower = $_POST['loan_borrower'];
	$loan_date_borrowed = $_POST['loan_date_borrowed'];
	$loan_due_date = $_POST['loan_due_date'];
	$loan_amount = $_POST['loan_amount'];

	$query = "UPDATE loan SET loan_borrower = '$loan_borrower', loan_date_borrowed = '$loan_date_borrowed', loan_due_date = '$loan_due_date', loan_amount = $loan_amount WHERE loan_id = $loan_id";
	
	if(mysqli_query($con, $query)){
		echo "success";
	}else{
		echo "failed";
	}

?>
<?php
 	
 	require "db_config.php";

 	//inserting data to loan
	$loan_borrower = $_POST["editName"];
	$loan_date_borrowed = $_POST["editDate"];
	$loan_due_date = $_POST["editDue"];
	$loan_due_time = $_POST["etTime"];
	$loan_amount = $_POST["editLoanAmount"];
	$time = date("G:i", strtotime($loan_due_time));

	$user_id = $_GET['user_id'];
	
	$query = "INSERT INTO loan (`user_id`, `loan_borrower`, `loan_date_borrowed`, `loan_due_date`, `loan_due_time`, `loan_amount`, `loan_status`) 
			  VALUES($user_id, '$loan_borrower', '$loan_date_borrowed', '$loan_due_date', '$time', $loan_amount, 'active');";

	if(mysqli_query($con, $query)){
		echo "success";
	}else{
		echo "failed";
	}

?>
<?php
 	
 	require "db_config.php";

 	$loan_id = $_GET['loan_id'];

	$query = "SELECT * FROM loan WHERE loan_id = $loan_id;";

	$res = mysqli_query($con, $query);

	$row = mysqli_fetch_array($res);

	if($row != NULL){
		$result = array();
		array_push($result,array(
		 "loan_id"=>$row['loan_id'],
		 "user_id"=>$row['user_id'],
		 "loan_borrower"=>$row['loan_borrower'],
		 "loan_date_borrowed"=>$row['loan_date_borrowed'],
		 "loan_due_date"=>$row['loan_due_date'],
		 "loan_due_time"=>$row['loan_due_time'],
		 "loan_amount"=>$row['loan_amount'],
		 "loan_status"=>$row['loan_status'],
		 "status"=>'success'
		 )
		);
		 
		echo json_encode(array("result"=>$result));
		 
		mysqli_close($con);
	}else{
		echo 'No available loan!';
	}
?>

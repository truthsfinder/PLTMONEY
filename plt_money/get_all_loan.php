<?php
 	
 	require "db_config.php";

 	if($_SERVER['REQUEST_METHOD']=='GET'){

 		$user_id = $_GET['user_id'];
	 	
	 	$query = "SELECT * FROM `loan` WHERE user_id = $user_id AND loan_status = 'active'";
	 
	 	$res = mysqli_query($con,$query);
	 	
		$result = array();
		
		while($row = mysqli_fetch_array($res)){
			array_push($result,
				array('loan_id'=>$row[0],
				'user_id'=>$row[1],
				'loan_borrower'=>$row[2],
				'loan_date_borrowed'=>$row[3],
				'loan_due_date'=>$row[4],
				'loan_amount'=>$row[5],
				'loan_status'=>$row[6],
				"status"=>'success'
			));
		}
		 
		echo json_encode(array("result"=>$result));
		 
		mysqli_close($con);
	 
	 }
?>

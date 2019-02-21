<?php
 	
 	require "db_config.php";

 	if($_SERVER['REQUEST_METHOD']=='GET'){

 		$user_id = $_GET['user_id'];
	 
	 	$query = "SELECT * FROM `category` 
	 		WHERE `user_id` = $user_id AND
			category_status = 'active'";
	 
	 	$res = mysqli_query($con,$query);
	 	
		$result = array();
		
		while($row = mysqli_fetch_array($res)){
			array_push($result,
				array('category_id'=>$row[0],
				'user_id'=>$row[1],
				'category_name'=>$row[2],
				'category_status'=>$row[3],
				"status"=>'success'
			));
		}
		 
		echo json_encode(array("result"=>$result));
		 
		mysqli_close($con);
	 
	 }
?>

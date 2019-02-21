<?php
 	
 	require "db_config.php";

 	//inserting data to category
	$category_name = $_POST["category_name"];

	$user_id = $_GET['user_id'];

	$query1 = "SELECT * FROM `category` 
	 		WHERE `category_name` = '$category_name' AND user_id = $user_id";

	$query2 = "INSERT INTO category (`user_id`, `category_name`, `category_status`) 
			  VALUES($user_id, '$category_name', 'active');";

	$res1 = mysqli_query($con, $query1);

	$row1 = mysqli_fetch_array($res1);

	if($row1 == NULL){
		if(mysqli_query($con, $query2)){
			echo "success";
		}else{
			echo "failed";
		}
	}else{
		echo 'duplicate';
	}

	
	
	

?>
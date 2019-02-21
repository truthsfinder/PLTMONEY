<?php
 	
 	require "db_config.php";

 	//update category
	$category_id = $_GET['category_id'];

	$category_name = $_POST['category_name'];

	$query = "UPDATE category SET category_name = '$category_name' WHERE category_id = $category_id";
	
	if(mysqli_query($con, $query)){
		echo "success";
	}else{
		echo "failed";
	}

?>
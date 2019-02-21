<?php
 	
 	require "db_config.php";

 	//delete category
	$category_id = $_GET['category_id'];

	$query = "DELETE from category WHERE category_id = $category_id";
	
	if(mysqli_query($con, $query)){
		echo "success";
	}else{
		echo "failed";
	}

?>
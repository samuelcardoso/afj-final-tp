<?php
$i = 0;

$i++;
$cfg['Servers'][$i]['verbose'] = 'Purchase DB';
$cfg['Servers'][$i]['host'] = 'purchase-ms-db';
$cfg['Servers'][$i]['port'] = '3306';
$cfg['Servers'][$i]['user'] = 'root';
$cfg['Servers'][$i]['password'] = 'rootpassword';
$cfg['Servers'][$i]['auth_type'] = 'config';

$i++;
$cfg['Servers'][$i]['verbose'] = 'Stock DB';
$cfg['Servers'][$i]['host'] = 'stock-ms-db';
$cfg['Servers'][$i]['port'] = '3306';
$cfg['Servers'][$i]['user'] = 'root';
$cfg['Servers'][$i]['password'] = 'rootpassword';
$cfg['Servers'][$i]['auth_type'] = 'config';

$i++;
$cfg['Servers'][$i]['verbose'] = 'User DB';
$cfg['Servers'][$i]['host'] = 'user-ms-db';
$cfg['Servers'][$i]['port'] = '3306';
$cfg['Servers'][$i]['user'] = 'root';
$cfg['Servers'][$i]['password'] = 'rootpassword';
$cfg['Servers'][$i]['auth_type'] = 'config';
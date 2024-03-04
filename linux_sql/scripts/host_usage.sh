#! /bin/sh

# CLI Arguments
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# Check # of args
if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

# save hostname as a variable
hostname=$(hostname -f)

# usage info
memory_free=$(vmstat --unit M | tail -1 | awk -v col="4" '{print $col}')
cpu_idle=$(vmstat --unit M | tail -1 | awk '{print $15}')
cpu_kernel=$(vmstat --unit M | tail -1 | awk '{print $14}')
disk_io=$(vmstat --unit M -d | tail -1 | awk -v col="10" '{print $col}')
disk_available=$(df / | awk 'NR==2 {print 100-$5}')
timestamp=$(date '+%Y-%m-%d %H:%M:%S') # current timestamp in `2019-11-26 14:40:19` format; use `date` cmd

# Subquery to get the host_id
subquery_id="(SELECT id FROM host_info WHERE hostname = '$hostname')"

insert_stmt="INSERT INTO host_usage ("timestamp", host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available) VALUES('$timestamp', $subquery_id, '$memory_free', '$cpu_idle', '$cpu_kernel', '$disk_io', '$disk_available');"

#env var for pql command
export PGPASSWORD=$psql_password

# Connect to DB
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"

exit $?

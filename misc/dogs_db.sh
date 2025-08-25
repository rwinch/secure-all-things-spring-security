#!/usr/bin/env bash


docker compose up -d --force-recreate

sleep 5

function run_psql() {
  local sql_input="$1"
  PGPASSWORD=secret psql -U myuser -h localhost mydatabase <<EOF
$sql_input
EOF
}

run_psql "drop table vector_store "



run_psql "drop table event_publication "
run_psql "drop table dog "
run_psql "`cat dogs.sql`"


run_psql "update dog set owner='josh' where id = 65  "
run_psql "update dog set owner='josh' where id = 71  "
run_psql "update dog set owner='josh' where id = 3   "
run_psql "update dog set owner='rob' where id = 45   "

run_psql "select id, name from dog"
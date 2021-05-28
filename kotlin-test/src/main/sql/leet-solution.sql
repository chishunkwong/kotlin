-- https://leetcode.com/problems/consecutive-numbers

DROP TABLE IF EXISTS leet_consec_nums

CREATE TABLE leet_consec_nums(
    num VARCHAR,
    id NUMERIC
);

with cte as (
    select num, lag(num, 1) over (order by id) last_one, lag(num, 2) over (order by id) last_two from leet_consec_nums
)
select num ConsecutiveNums from cte where num = last_one and num = last_two;

-- end

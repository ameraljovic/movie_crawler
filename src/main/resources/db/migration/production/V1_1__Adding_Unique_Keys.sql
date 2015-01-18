alter table movies
add constraint movie_unique_key unique (tmdb_id);
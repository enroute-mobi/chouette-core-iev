--
-- Name: area_parent_fkey; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY stop_areas
    ADD CONSTRAINT area_parent_fkey FOREIGN KEY (parent_id) REFERENCES stop_areas(id) ON DELETE SET NULL;


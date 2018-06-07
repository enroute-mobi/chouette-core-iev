ALTER TABLE ONLY public.import_resources    ADD CONSTRAINT fk_rails_b84922ee37 FOREIGN KEY (referential_id) REFERENCES public.referentials(id);

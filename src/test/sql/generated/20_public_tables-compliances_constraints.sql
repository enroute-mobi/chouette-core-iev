ALTER TABLE ONLY public.compliance_check_messages    ADD CONSTRAINT fk_rails_1361178dd5 FOREIGN KEY (compliance_check_id) REFERENCES public.compliance_checks(id);
ALTER TABLE ONLY public.compliance_checks    ADD CONSTRAINT fk_rails_2cbc8a0142 FOREIGN KEY (compliance_check_set_id) REFERENCES public.compliance_check_sets(id);
ALTER TABLE ONLY public.compliance_check_resources    ADD CONSTRAINT fk_rails_45cd323eca FOREIGN KEY (compliance_check_set_id) REFERENCES public.compliance_check_sets(id);
ALTER TABLE ONLY public.compliance_check_messages    ADD CONSTRAINT fk_rails_70bd95092e FOREIGN KEY (compliance_check_resource_id) REFERENCES public.compliance_check_resources(id);
ALTER TABLE ONLY public.compliance_check_blocks    ADD CONSTRAINT fk_rails_7d7a89703f FOREIGN KEY (compliance_check_set_id) REFERENCES public.compliance_check_sets(id);
ALTER TABLE ONLY public.compliance_check_sets    ADD CONSTRAINT fk_rails_d61509f1a9 FOREIGN KEY (workbench_id) REFERENCES public.workbenches(id);
ALTER TABLE ONLY public.compliance_checks    ADD CONSTRAINT fk_rails_df077b5b35 FOREIGN KEY (compliance_check_block_id) REFERENCES public.compliance_check_blocks(id);
ALTER TABLE ONLY public.compliance_check_messages    ADD CONSTRAINT fk_rails_e1cf9ec59a FOREIGN KEY (compliance_check_set_id) REFERENCES public.compliance_check_sets(id);

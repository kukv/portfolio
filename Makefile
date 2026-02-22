deploy/%:
	git tag $(@F) && \
	git push origin $(@F)

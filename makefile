JFLAGS = -g
JC = javac
NAME = "Main"
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLASGS) $*.java

CLASSES = \
	Main.java \
	Toolbar.java \
	ImageCollectionModel.java \
	ImageCollectionView.java \
	ImageModel.java \
	ImageView.java

default: classes

classes: $(CLASSES:.java=.class)

run: classes
	@echo "Running"
	java $(NAME)

clean:
	$(RM) *.class



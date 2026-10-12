# ubicado en la raiz del repo

.PHONY: install install-tools install-hooks install-deps

# instala todo de una
install: install-tools install-hooks install-deps

# instala gestores y las herramientas del sistema
install-tools:
	@echo "Instalando herramientas del sistema...."
	sudo apt install -y clang-format
	@echo "Isntalando herramientas Python (requiere pipx)...."
	@echo "Si falla, instalar pipx primero: sudo apt install pipx"
	@echo "Node.js y npm deben instalarse via nodesource:"
	@echo "curl -fsSL https://deb.nodesource.com/setup_lts.x | sudo -E bash -"
	@echo "sudo apt install -y nodejs"
	pipx install pre-commit
	pipx install commitizen
	pipx install platformio

# activa los hooks en el repo
install-hooks:
	@echo "Activando hooks de Git...."
	pre-commit install
	pre-commit install --hook-type commit-msg
	pre-commit install --hook-type pre-push

# instala dependencias de cada componente
install-deps:
	@echo "Instalando dependencias del frontend...."
	cd frontend/dashboard && npm install
	@echo "Instalando dependencias del backend java...."
	cd backend/spring/final-project && ./gradlew dependencies

#checkeo que todo se instalo correctamente
check:
	@echo "Verificando instalaciones...."
	@java -version
	@mvn --version
	@node --version
	@npm --version
	@pre-commit --version
	@cz version
	@pio --version

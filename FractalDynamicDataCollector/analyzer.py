import js2py
import re


class DynamicFractalAnalyzer:
    def __init__(self, *args, **kwargs) -> None:
        pass

    def __get_branches(self, script_text: str):
        return js2py.eval_js(script_text)

    def __remove_context_content(self, script_text: str) -> str:
        script_text = re.sub(r"context\.[^\n]+\n", "", script_text)
        script_text = re.sub(r"\n[^\n]+document\.[^\n]+\n", "", script_text)
        script_text = re.sub(
            r"\s+(const|var|let)\s+([^\s]+)\s*=\s*\w+\.getContext\(\s*['\"]2d[\"']\s*\)[^\n;]*[\n;]",
            r"\1 \2 = {};",
            script_text,
        )
        return script_text

    def load_fractal(self, fractal_script_path: str) -> str:
        with open(fractal_script_path, "r", encoding="utf-8") as file:
            fractal_content = file.read()
        fractal_content = self.__remove_context_content(fractal_content)
        branch_data_script = (
            fractal_content
            + "; function getData() { return JSON.stringify(globalResult); } getData();"
        )
        return self.__get_branches(branch_data_script)

    def load_data_from_fractal(
        self, fractal_script_path: str, variable_name: str
    ) -> str:
        with open(fractal_script_path, "r", encoding="utf-8") as file:
            fractal_content = file.read()
        fractal_content = self.__remove_context_content(fractal_content)
        branch_data_script = (
            fractal_content
            + "; function getData() { return JSON.stringify("
            + variable_name
            + " ); } getData();"
        )
        return self.__get_branches(branch_data_script)

    def load_fractal_output_only(self, fractal_script_path: str):
        with open(fractal_script_path, "r", encoding="utf-8") as file:
            fractal_content = file.read()
        fractal_content = self.__remove_context_content(fractal_content)
        print(self.__get_branches(fractal_content))

    def test(self):
        self.load_fractal_output_only("example/1/js/platnoJS.js")


if __name__ == "__main__":
    dyn = DynamicFractalAnalyzer()
    dyn.test()

import json


def analyze_key_value(key, value, possible_values):
    if key not in possible_values:
        possible_values[key] = dict()
    if value not in possible_values[key].keys():
        possible_values[key][value] = 1
    else:
        possible_values[key][value] = possible_values[key][value] + 1


def parse(element, depth, possible_values):
    if type(element) is list:
        for sub_element in element:
            parse(sub_element, depth + 1, possible_values)
    elif type(element) is dict:
        for key, value in element.items():
            if type(value) is str:
                analyze_key_value(key, value, possible_values)
            elif type(value) is list:
                for sub_value in value:
                    if type(value) is str or type(value) is int or type(value) is float or type(value) is bool:
                        analyze_key_value(key, sub_value, possible_values)
                    else:
                        parse(sub_value, depth + 1, possible_values)
            else:
                parse(value, depth + 1, possible_values)
    else:
        if type(element) is not str and type(element) is not int and type(element) is not float and \
                type(element) is not bool and element:
            print("Not used")
            print(type(element))


def load_json(file_name):
    with open(file_name, "r", encoding="utf-8") as file:
        return json.loads(file.read())


def print_possible_values(possible_values):
    for key, value_dict in possible_values.items():
        for value, occurrence in value_dict.items():
            print(key + " " + value + " -> " + str(occurrence))


if __name__ == "__main__":
    ast = load_json("AST.json")
    possible_values2 = dict()
    parse(ast, 0, possible_values2)
    print_possible_values(possible_values2)

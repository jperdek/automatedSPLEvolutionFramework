export class DecoratorTypesService {
  static wholeInitialization(arg0: {}) {
  }

  static wholeClass(arg0: any): (target: any) => void  {
    return (target: any) => {
    }
  }

  static wholeBlockMethod(arg0: any): (target: any, propertyKey: string | symbol | undefined, descriptor: any) => void {
    return (target: any, propertyKey: string | symbol | undefined, descriptor: any) => {
    }
  }

  static variableParameter(arg0: {}, commentedLine: string | null = null): (target: any, propertyKey: string | symbol | undefined, parameterIndex: number) => void {
    return (target: any, propertyKey: string | symbol | undefined, parameterIndex: number) => {
    }
  }

  static variableDeclarationLocal(arg0: {}, commentedLine: string | null = null): (target: any, propertyKey: string | symbol | undefined, parameterIndex: number) => void {
    return (target: any, propertyKey: string | symbol | undefined, parameterIndex: number) => {
    }
  }

  static variableDeclarationClass(arg0: {}, commentedLine: string | null = null): (target: any, key: string)=> void {
    return (target: any, key: string) => {
    }
  }

  static skipLine(arg0: {}, commentedLine: string | null = null): (target: any, propertyKey: string | symbol | undefined, parameterIndex: number) => void {
    return (target: any, propertyKey: string | symbol | undefined, parameterIndex: number) => {
    }
  }

  constructor() { }